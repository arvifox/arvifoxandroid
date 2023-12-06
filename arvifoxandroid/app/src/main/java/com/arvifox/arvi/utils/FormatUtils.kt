package com.arvifox.arvi.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.strikeThrough
import androidx.core.text.superscript
import androidx.fragment.app.FragmentActivity
import com.arvifox.arvi.R
import java.io.ByteArrayOutputStream
import java.io.InputStream

object FormatUtils {

    fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    fun spannable() = SpannableStringBuilder()
        .bold { append("bold text") }
        .color(R.color.colorPrimary) { bold { append("colored bold text") } }
        .strikeThrough { append("strike") }
        .superscript { append("super") }
        .append("regular")

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

    /**
     * Shows a [Toast] on the UI thread.
     *
     * "зфкфь еуче Еру ьуыыфпу ещ ырщц
     */
    fun FragmentActivity.showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}