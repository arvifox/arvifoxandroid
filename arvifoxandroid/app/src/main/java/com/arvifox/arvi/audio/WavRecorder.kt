package com.arvifox.arvi.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import java.io.*

class WavRecorder(path: String) {

    companion object {
        private const val RECORDER_BPP = 16
        private const val AUDIO_RECORDER_FOLDER = "AudioRecorder"
        private const val AUDIO_RECORDER_TEMP_FILE = "record_temp.raw"
        private const val RECORDER_SAMPLE_RATE = 44100
        private const val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
        private const val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
    }

    private var audioData: ShortArray? = null
    private var recorder: AudioRecord? = null
    private var bufferSize = 0
    private var recordingThread: Thread? = null
    private var isRecording = false
    private var output: String? = null

    init {
        bufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING
        ) * 3

        // short array that pcm data is put into.
        audioData = ShortArray(bufferSize)
        output = path
    }

    //region public

    fun startRecording() {
        recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize
        )
        val i = recorder?.state
        if (i == 1)
            recorder?.startRecording()
        isRecording = true
        recordingThread = Thread(Runnable { writeAudioDataToFile() }, "AudioRecorder Thread")
        recordingThread?.start()
    }

    //endregion

    //region private

    private fun getFilename(): String {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File(filepath,
                AUDIO_RECORDER_FOLDER
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + output!!
    }

    private fun getTempFilename(): String {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File(filepath,
                AUDIO_RECORDER_FOLDER
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        val tempFile = File(filepath,
                AUDIO_RECORDER_TEMP_FILE
        )
        if (tempFile.exists())
            tempFile.delete()
        return file.absolutePath + "/" + AUDIO_RECORDER_TEMP_FILE
    }

    //endregion

    private fun writeAudioDataToFile() {
        val data = ByteArray(bufferSize)
        val filename = getTempFilename()
        var os: FileOutputStream? = null

        try {
            os = FileOutputStream(filename)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        var read = 0
        if (null != os) {
            while (isRecording) {
                read = recorder!!.read(data, 0, bufferSize)
                if (read > 0) {
                }

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            try {
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun stopRecording() {
        if (null != recorder) {
            isRecording = false

            val i = recorder?.state
            if (i == 1)
                recorder?.stop()
            recorder?.release()

            recorder = null
            recordingThread = null
        }

        copyWaveFile(getTempFilename(), getFilename())
        deleteTempFile()
    }

    private fun deleteTempFile() {
        val file = File(getTempFilename())
        file.delete()
    }


    private fun copyWaveFile(inFilename: String, outFilename: String) {
        var inStream: FileInputStream?
        var outStream: FileOutputStream?
        var totalAudioLen: Long = 0
        var totalDataLen = totalAudioLen + 36
        val longSampleRate = RECORDER_SAMPLE_RATE.toLong()
        val channels = if (RECORDER_CHANNELS == AudioFormat.CHANNEL_IN_MONO) 1 else 2
        val byteRate = (RECORDER_BPP * RECORDER_SAMPLE_RATE * channels / 8).toLong()
        val data = ByteArray(bufferSize)
        try {
            inStream = FileInputStream(inFilename)
            outStream = FileOutputStream(outFilename)
            totalAudioLen = inStream.channel.size()
            totalDataLen = totalAudioLen + 36
            writeWaveFileHeader(
                    outStream,
                    totalAudioLen,
                    totalDataLen,
                    longSampleRate,
                    channels,
                    byteRate
            )
            while (inStream.read(data) != -1) {
                outStream.write(data)
            }
            inStream.close()
            outStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    private fun writeWaveFileHeader(
            out: FileOutputStream, totalAudioLen: Long,
            totalDataLen: Long, longSampleRate: Long, channels: Int, byteRate: Long
    ) {
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // format = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] =
                ((if (RECORDER_CHANNELS == AudioFormat.CHANNEL_IN_MONO) 1 else 2) * 16 / 8).toByte() // block align
        header[33] = 0
        header[34] = RECORDER_BPP.toByte() // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        out.write(header, 0, 44)
    }
}