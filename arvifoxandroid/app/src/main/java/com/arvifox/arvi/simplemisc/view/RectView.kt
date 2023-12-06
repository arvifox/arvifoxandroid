package com.arvifox.arvi.simplemisc.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.arvifox.arvi.R

class RectView @JvmOverloads constructor(
        context: Context,
        attrSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrSet, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
                attrSet,
                R.styleable.RectView,
                R.attr.rectViewStyle,
                R.style.RectViewStyle
        )
        try {
            paint.color = typedArray.getColor(
                    R.styleable.RectView_rectColor,
                    Color.BLUE
            )
        } finally {
            typedArray.recycle()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}