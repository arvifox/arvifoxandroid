package com.arvifox.arvi.audio

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import com.microsoft.cognitiveservices.speech.audio.PullAudioOutputStream
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.IOException

class FoxxAudioManager(cnt: Context) : IAudioManager {

    private var audioManager: AudioManager =
            cnt.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val asm: AssetManager = cnt.assets

    init {
        audioManager.isSpeakerphoneOn = false
    }

    private lateinit var wavRecorder: WavRecorder

    override fun prepareForRecording(filename: String) {
        wavRecorder = WavRecorder(filename)
    }

    override fun startRecording() {
        wavRecorder.startRecording()
    }

    override fun stopRecording() {
        wavRecorder.stopRecording()
    }

    override fun setSpeakerOn() {
        audioManager.mode = AudioManager.MODE_IN_CALL
        audioManager.isSpeakerphoneOn = true
    }

    override fun setModeSpeaker(ms: Pair<Int, Boolean>) {
        audioManager.isSpeakerphoneOn = ms.second
        audioManager.mode = ms.first
    }

    override fun getModeSpeaker(): Pair<Int, Boolean> =
            audioManager.mode to audioManager.isSpeakerphoneOn

    @SuppressLint("NewApi")
    override fun playAzureAudioStream(stream: PullAudioOutputStream) {
        // Text-to-speech audio associated with the activity is 16 kHz 16-bit mono PCM data
        val sampleRate = 8000
        val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        )
        val attrBuilder = AudioAttributes.Builder()
        attrBuilder.setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
        attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        val audioFormat = AudioFormat.Builder()
        audioFormat.setEncoding(AudioFormat.ENCODING_PCM_16BIT)
        audioFormat.setSampleRate(sampleRate)
        val track = AudioTrack(
                attrBuilder.build(),
                audioFormat.build(),
                bufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        track.setVolume(AudioTrack.getMaxVolume())
        track.play()
        track.setVolume(AudioTrack.getMaxVolume())
        track.notificationMarkerPosition = 12
        track.setPlaybackPositionUpdateListener(object :
                AudioTrack.OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(p0: AudioTrack?) {}
            override fun onPeriodicNotification(p0: AudioTrack?) {}
        })
        // Audio is streamed as it becomes available. Play it as it arrives.
        val buffer = ByteArray(bufferSize)
        var bytesRead: Long
        do {
            bytesRead = stream.read(buffer)
            track.write(buffer, 0, bytesRead.toInt())
        } while (bytesRead == bufferSize.toLong())
        track.release()
    }

    private fun foo() {
        // Get the length of the audio stored in the file (16 bit so 2 bytes per short)
        // and create a short array to store the recorded audio.
        val fil = File("//android_asset/qwer.wav")
        val pcmFile = asm.open("qwer.wav")
        val audioLength = (fil.length() / 2).toInt()
        val audioData = ShortArray(audioLength)
        var dis: DataInputStream? = null

        try {
            // Create a DataInputStream to read the audio data back from the saved file.
            val `is` = pcmFile
            val bis = BufferedInputStream(`is`)
            dis = DataInputStream(bis)

            // Read the file into the music array.
            var i = 0
            while (dis!!.available() > 0) {
                audioData[i] = dis!!.readShort()
                i++
            }

            // Create a new AudioTrack using the same parameters as the AudioRecord.
            val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, 8000, 4,
                    2, audioLength, AudioTrack.MODE_STREAM)
            audioTrack.setNotificationMarkerPosition(audioLength)
            audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onPeriodicNotification(track: AudioTrack) {
                    // nothing to do
                }

                override fun onMarkerReached(track: AudioTrack) {
                    Log.d("foxx", "Audio track end of file reached...")
                    //messageHandler.sendMessage(messageHandler.obtainMessage(PLAYBACK_END_REACHED))
                }
            })

            // Start playback
            audioTrack.play()

            // Write the music buffer to the AudioTrack object
            audioTrack.write(audioData, 0, audioLength)

        } catch (e: Exception) {
            Log.e("foxx", "Error playing audio.", e)
        } finally {
            if (dis != null) {
                try {
                    dis!!.close()
                } catch (e: IOException) {
                    // don't care
                }

            }
        }
    }
}