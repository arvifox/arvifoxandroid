package com.arvifox.arvi.simplemisc.camera

import android.annotation.SuppressLint
import android.media.Image
import com.arvifox.arvi.utils.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Saves a JPEG [Image] into the specified [File].
 */
internal class ImageSaverToFile(
        private val image: Image,
        private val file: File
) : Runnable {

    @SuppressLint("NewApi")
    override fun run() {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        var output: FileOutputStream? = null
        try {
            output = FileOutputStream(file).apply {
                write(bytes)
            }
        } catch (e: IOException) {
            Logger.d { e.toString() }
        } finally {
            image.close()
            output?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    Logger.e { "" + e.toString() }
                }
            }
        }
    }
}