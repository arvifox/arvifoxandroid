package com.arvifox.arvi.utils.views

import android.graphics.*
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

object Viewuu {

    @BindingAdapter("app:filter")
    fun ImageView.applyFilter(prev: String, value: String) {
        colorFilter = null
        if (value == "grey") {
            val matrix = ColorMatrix()
            matrix.setSaturation(0.0F)
            val cf = ColorMatrixColorFilter(matrix)
            colorFilter = cf
        }
    }

    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline: Float = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.0f).toInt()
        val height = (baseline + paint.descent() + 0.0f).toInt()
        val image: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0.0F, baseline, paint)
        return image
    }

    fun ImageView.setTextBitmap(text: String, textSize: Float, textColor: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val lines = text.split("\n")
        var maxWidth = 0
        for (line in lines) {
            val width = paint.measureText(line).toInt()
            if (width > maxWidth) {
                maxWidth = width
            }
        }
        val height = paint.descent() - paint.ascent()
        val bitmap = Bitmap.createBitmap(maxWidth, height.toInt() * lines.size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        var y = - paint.ascent()
        for (line in lines) {
            canvas.drawText(line, 0f, y, paint)
            y += height
        }
        setImageBitmap(bitmap)
    }

    fun setFab(fab: FloatingActionButton) {
        fab.setTextBitmap("", 40f, Color.RED)
        fab.scaleType = ImageView.ScaleType.CENTER
        fab.adjustViewBounds = false
    }
}