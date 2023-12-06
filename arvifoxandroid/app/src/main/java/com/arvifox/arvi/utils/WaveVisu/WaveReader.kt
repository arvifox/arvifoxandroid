package com.arvifox.arvi.utils.WaveVisu

import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import kotlin.math.min

class WaveReader {

    var chunkId = 0; private set
    var chunkSize = 0; private set
    var format = 0; private set
    var subchunk1Id = 0; private set
    var subchunk1Size = 0; private set
    var audioFormat: Short = 0; private set
    var numChannels: Short = 0; private set
    var sampleRate = 0; private set
    var byteRate = 0; private set
    var blockAlign: Short = 0; private set
    var bitsPerSample: Short = 0; private set
    var subchunk2Id = 0; private set
    var subchunk2Size = 0; private set

    private var bytesBuff: ByteArray? = null
    private val rawBytesBuff = ByteArray(32 * 100) { 0 }

    var bytesRead = 0; private set
    var isHeaderRead = false; private set

    val waveSamples: Int
        get() = subchunk2Size / (bitsPerSample / 8)

    fun readWaveHeaderFromAssets(context: Context, filename: String) {
        close()

        try {
            bytesBuff = context.assets.open(filename).readBytes()
            isHeaderRead = readHeader()
        } catch (e: Exception) {
            throw FileNotFoundException("Cannot load file $filename from assets, error message: ${e.message}")
        }
    }

    fun readWaveHeaderFromFile(filename: String) = readWaveHeaderFromFile(File(filename))

    fun readWaveHeaderFromFile(file: File) {
        close()

        try {
            bytesBuff = file.readBytes()
            isHeaderRead = readHeader()
        } catch (e: Exception) {
            throw FileNotFoundException("Cannot load file ${file.name}, error message: ${e.message}")
        }
    }

    private fun readHeader(): Boolean {
        val bytes = bytesBuff ?: return false

        chunkId = Utils.bytesToIntLittleEndian(bytes, 0)
        chunkSize = Utils.bytesToIntLittleEndian(bytes, 4)
        format = Utils.bytesToIntLittleEndian(bytes, 8)
        subchunk1Id = Utils.bytesToIntLittleEndian(bytes, 12)
        subchunk1Size = Utils.bytesToIntLittleEndian(bytes, 16)
        audioFormat = Utils.bytesToShortLittleEndian(bytes, 20).toShort()
        numChannels = Utils.bytesToShortLittleEndian(bytes, 22).toShort()
        sampleRate = Utils.bytesToIntLittleEndian(bytes, 24)
        byteRate = Utils.bytesToIntLittleEndian(bytes, 28)
        blockAlign = Utils.bytesToShortLittleEndian(bytes, 32).toShort()
        bitsPerSample = Utils.bytesToShortLittleEndian(bytes, 34).toShort()
        subchunk2Id = Utils.bytesToIntLittleEndian(bytes, 36)
        subchunk2Size = Utils.bytesToIntLittleEndian(bytes, 40)

        bytesRead = HEADER_SIZE

        return true
    }

    fun isHeaderValid(): Boolean {
        if (!isHeaderRead)
            throw unreadWaveHeaderException

        return chunkId == MAGIC_RIFF && format == MAGIC_WAVE && subchunk1Id == MAGIC_FMT && subchunk2Id == MAGIC_DATA
    }

    /**
     * PCM format - encoding without compression (linear)
     * */
    fun isPCMFormat(): Boolean = subchunk1Size == 16 && audioFormat == 1.toShort()

    fun readRaw8bit(buffer: ByteArray): Int {
        if (bitsPerSample != 8.toShort())
            throw WrongDepthReading(8, bitsPerSample)

        return readRaw(buffer, buffer.size)
    }

    fun readRaw16bit(buffer: ShortArray): Int {
        if (bitsPerSample != 16.toShort())
            throw WrongDepthReading(16, bitsPerSample)

        val needBytes = buffer.size * 2
        var currentBytes = 0
        var currentReadBytes = 0
        var currentPos = 0

        do {
            val toRead = min(needBytes - currentBytes, rawBytesBuff.size)
            currentReadBytes = readRaw(rawBytesBuff, toRead)
            currentBytes += currentReadBytes

            for (i in 0 until currentReadBytes) {
                if (i % 2 == 0)
                    continue

                buffer[currentPos++] = Utils.bytesToShortLittleEndian(rawBytesBuff, i - 1).toShort()
            }

        } while (currentBytes != needBytes && currentReadBytes != 0)

        return currentBytes / 2
    }

    fun readRaw32bit(buffer: IntArray): Int {
        if (bitsPerSample != 32.toShort())
            throw WrongDepthReading(32, bitsPerSample)

        val needBytes = buffer.size * 4
        var currentBytes = 0
        var currentReadBytes = 0
        var currentPos = 0

        do {
            val toRead = min(needBytes - currentBytes, rawBytesBuff.size)
            currentReadBytes = readRaw(rawBytesBuff, toRead)
            currentBytes += currentReadBytes

            for (i in 0 until currentReadBytes) {
                if (i % 4 == 3)
                    continue

                buffer[currentPos++] = Utils.bytesToIntLittleEndian(rawBytesBuff, i - 3)
            }

        } while (currentBytes != needBytes && currentReadBytes != 0)

        return currentBytes / 4
    }

    fun readRaw(buffer: ByteArray, length: Int): Int {
        val bytes = bytesBuff ?: throw unreadWaveHeaderException
        if (length > buffer.size)
            throw IndexOutOfBoundsException("Requested length ($length) is greater than buffer length (${buffer.size})")

        var toRead = min(buffer.size, length)
        toRead = min(toRead, bytes.size - bytesRead)
        bytes.copyInto(buffer, 0, bytesRead, bytesRead + toRead)
        bytesRead += toRead
        return toRead
    }

    /**
     * Calculates average value for each channel by all [samples]
     *
     * @param samples - number of samples to read
     * @return average values for each channel
     * */
    fun readAverage(samples: Int): DoubleArray {
        val bytes = bytesBuff ?: throw unreadWaveHeaderException
        val leftBytes = subchunk2Size - bytesRead + HEADER_SIZE
        val bytesLength = samples * bitsPerSample / 8
        if (bytesLength > leftBytes)
            throw LengthOutOfBoundsException(bytesLength, leftBytes)

        val channels = numChannels.toInt()
        val result = DoubleArray(channels)

        var currentBytes = 0
        var currentChannel = 0
        while (currentBytes < bytesLength) {

            val offset = bytesRead + currentBytes
            val amp = when (bitsPerSample / 8) {
                1 -> bytes[offset].toInt()
                2 -> Utils.bytesToShortLittleEndian(bytes, offset)
                4 -> Utils.bytesToIntLittleEndian(bytes, offset)
                else -> throw RuntimeException("Unexpected depth: bitsPerSample=$bitsPerSample")
            }
            result[currentChannel] = result[currentChannel] + amp

            currentChannel++
            currentChannel %= channels
            currentBytes += bitsPerSample / 8
        }

        bytesRead += bytesLength

        for (i in 0 until channels) {
            result[i] = result[i] / samples
        }

        return result
    }

    fun close() {
        isHeaderRead = false
        bytesBuff = null
        bytesRead = 0
    }

    companion object {
        const val MAGIC_RIFF = 0x46464952
        const val MAGIC_WAVE = 0x45564157
        const val MAGIC_FMT = 0x20746D66
        const val MAGIC_DATA = 0x61746164

        const val HEADER_SIZE = 44

        val unreadWaveHeaderException = UnreadWaveHeaderException()
    }

    class UnreadWaveHeaderException : IllegalStateException("Unread wave header or an error occurred during reading")
    class WrongDepthReading(tryDepth: Int, actualDepth: Short) : RuntimeException("Trying to read raw wave with wrong depth, attempt=$tryDepth, actual=$actualDepth")
    class LengthOutOfBoundsException(requested: Int, left: Int) : IndexOutOfBoundsException("Requested length ($requested) is greater than left bytes ($left)")
}