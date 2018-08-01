package com.arvifox.arvi.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

object FormatUtils {

    fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

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