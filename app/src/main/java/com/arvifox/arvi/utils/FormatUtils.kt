package com.arvifox.arvi.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import java.io.ByteArrayOutputStream
import java.io.InputStream

object FormatUtils {

    fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    @SuppressLint("NewApi", "MissingPermission")
    fun Image.tobitmap(): Bitmap? {
        val bb = this.planes[0].buffer
        val bytes = ByteArray(bb.remaining())
        bb.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options())
    }

    fun InputStream.takeString(): String {
        val bar = ByteArrayOutputStream()
        val byar = ByteArray(1024)
        var len: Int = this.read(byar)
        while (len != -1) {
            bar.write(byar, 0, len)
            len = this.read(byar)
        }
        return bar.toString("UTF-8")
    }

    fun InputStream.takeByteArray(): ByteArray {
        val bar = ByteArrayOutputStream()
        val byar = ByteArray(1024)
        var len: Int = this.read(byar)
        while (len != -1) {
            bar.write(byar, 0, len)
            len = this.read(byar)
        }
        return bar.toByteArray()
    }
}