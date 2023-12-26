package com.arvifox.arvi.simplemisc.anim.rev

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.arvifox.arvi.utils.dip2px
import com.arvifox.arvi.utils.dpToPx

class Kruva @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var radius: Float = dip2px(30)
    val paint = Paint().apply {
        strokeWidth = 3F.dpToPx(context.resources.displayMetrics).toFloat()
        //color = Color.parseColor("#986323")
        color = Color.RED
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val h = PropertyValuesHolder.ofObject("myclo", ArgbEvaluator(), Color.RED, Color.MAGENTA)
        val r = PropertyValuesHolder.ofFloat("myra", 30F, 80F)
        ValueAnimator.ofPropertyValuesHolder(h, r).apply {
            duration = 1000
            startDelay = 500
            interpolator = Fonte()
            addUpdateListener {
                radius = dip2px(it.getAnimatedValue("myra") as Float)
                paint.color = it.getAnimatedValue("myclo") as Int
                invalidate()
            }
        }.start()
    }

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        ValueAnimator.ofInt(30, 80).apply {
//            duration = 1000
//            startDelay = 500
//            addUpdateListener {
//                radius = dip2px(it.animatedValue as Int)
//                invalidate()
//            }
//        }.start()
//    }
}