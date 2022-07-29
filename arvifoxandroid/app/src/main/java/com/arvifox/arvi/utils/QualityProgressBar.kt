package com.arvifox.arvi.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.arvifox.arvi.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class QualityProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var strokeWidth = 25f
        set(value) {
            if (value > 0f) {
                field = value
                invalidate()
                requestLayout()
            }
        }

    var totalAnimationDuration = 10 * 1000L
        set(value) {
            if (value > 0)
                field = value
        }

    var recolorAnimationDuration = 500L
        set(value) {
            if (value > 0)
                field = value
        }

    var text = ""
        set(value) {
            field = value
            calculatedTextSize = false
            invalidate()
            requestLayout()
        }

    var textColor = 0
        set(value) {
            field = value
            textPaint.color = value
            invalidate()
        }

    var colorUnspecified: Int = Color.GRAY
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var colorStroke: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var idleStrokeWidth = 8f
        set(value) {
            field = value
            idleStrokePaint.strokeWidth = value
            updateArcRect()
            invalidate()
        }

    var maxTextSize = 0f
        set(value) {
            field = max(value, 0f) * resources.displayMetrics.scaledDensity
            calculatedTextSize = false
            invalidate()
        }
        get() = field / resources.displayMetrics.scaledDensity

    var rotation = 0
        set(value) {
            field = value % 360
            invalidate()
        }

    /* Everything is inside this rectangle */
    private val drawRect = RectF()
    private var sweepAngle = 0f
    private val arcRect = RectF()

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = this@QualityProgressBar.strokeWidth
    }
    private val idleStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = idleStrokeWidth
    }
    private val textPaint = Paint()
    private val textBounds = Rect()

    private val qualityAnimators = mutableListOf<Pair<ColoredArc, ValueAnimator>>()
    private val arcs: List<Arc>

    private var progressAnimationEnded = false
    private var calculatedTextSize = false

    init {
        arcs = (0 until MAX_ARCS).map {
            Arc(it * ARC_STEP.toFloat(), ARC_STEP.toFloat(), paint, colorUnspecified)
        }

        context.theme.obtainStyledAttributes(attrs, R.styleable.QualityProgressBar, 0, 0).apply {
            try {
                strokeWidth = getDimension(R.styleable.QualityProgressBar_strokeWidth, 25f)
                totalAnimationDuration = getInteger(R.styleable.QualityProgressBar_totalAnimationDuration, 10000).toLong()
                recolorAnimationDuration = getInteger(R.styleable.QualityProgressBar_recolorAnimationDuration, 500).toLong()
                text = getString(R.styleable.QualityProgressBar_text) ?: ""
                textColor = getColor(R.styleable.QualityProgressBar_textColor, Color.DKGRAY)
                maxTextSize = getDimension(R.styleable.QualityProgressBar_maxTextSize, 0f)
                colorUnspecified = getColor(R.styleable.QualityProgressBar_colorUnspecified, Color.GRAY)
                colorStroke = getColor(R.styleable.QualityProgressBar_colorStroke, Color.LTGRAY)
                idleStrokeWidth = getDimension(R.styleable.QualityProgressBar_idleStrokeWidth, 8f)
                rotation = getInteger(R.styleable.QualityProgressBar_rotation, 0)
            } finally {
                recycle()
            }
        }

        idleStrokePaint.color = colorStroke
    }

    /**
     * Starts animation of progressBar, cancels previous animation if needed
     * */
    fun animateProgress() {
        /* Cancel previous animation, setup default parameters */
        qualityAnimators.forEach { it.second.cancel() }
        qualityAnimators.clear()
        arcs.forEach { it.color = colorUnspecified }
        progressAnimationEnded = false

        /* Animate sweepAngle */
        ValueAnimator.ofFloat(0f, 360f).apply {
            duration = totalAnimationDuration
            interpolator = LinearInterpolator()
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                invalidate()
            }

            /* OnEnd: Delegate invalidation process to color animators */
            addListener(onEnd = {
                qualityAnimators.lastOrNull()?.second?.addUpdateListener { invalidate() }
                progressAnimationEnded = true
            })
            start()
        }
    }

    fun setColorSec(fromSec: Int, toSec: Int, color: Int) = setColorMillis(fromSec * 1000L, toSec * 1000L, color)

    fun setColorMillis(fromMillis: Long, toMillis: Long, color: Int): Boolean {
        val fromDeg = (fromMillis.toFloat() / totalAnimationDuration * 360f).toInt()
        val toDeg = (toMillis.toFloat() / totalAnimationDuration * 360f).toInt()

        return setColorDeg(fromDeg, toDeg, color)
    }

    fun setColorDeg(fromDeg: Int, toDeg: Int, color: Int): Boolean {
        if (fromDeg < 0 || toDeg > 360)
            return false

        /* Dont change quality color if target has already changed color */
        qualityAnimators.forEach { (coloredArc, _) ->
            val range = coloredArc.fromDeg until coloredArc.toDeg
            if (fromDeg in range || toDeg in range)
                return false
        }

        /* Store animated arcs & valueAnimator of these arcs */
        qualityAnimators.add(ColoredArc(fromDeg, toDeg) to ValueAnimator.ofFloat(0f, 1f).apply {
            duration = recolorAnimationDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                for (i in fromDeg until toDeg)
                    arcs[i].color = argbEvaluator.evaluate(it.animatedFraction, colorUnspecified, color) as Int

                if (progressAnimationEnded)
                    invalidate()
            }
            start()
        })

        return true
    }

    fun setColorFromResSec(fromSec: Int, toSec: Int, colorId: Int) = setColorFromResMillis(fromSec * 1000L, toSec * 1000L, colorId)
    fun setColorFromResMillis(fromMillis: Long, toMillis: Long, colorId: Int) = setColorMillis(fromMillis, toMillis, ContextCompat.getColor(context, colorId))
    fun setColorFromResDeg(fromDeg: Int, toDeg: Int, colorId: Int) = setColorDeg(fromDeg, toDeg, ContextCompat.getColor(context, colorId))

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mw = MeasureSpec.getSize(widthMeasureSpec)
        val mh = MeasureSpec.getSize(heightMeasureSpec)

        val minSide = min(mw, mh)

        /* Calculate workspace square */
        val side = minSide.toFloat()
        val (x, y) = if (minSide == mw) {
            0f to (mh / 2f - side / 2)
        } else (mw / 2f - side / 2) to 0f

        drawRect.set(x + paddingStart, y + paddingTop, x + side - paddingEnd, y + side - paddingBottom)
        updateArcRect()

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        drawIdleStroke(canvas)

        paint.apply {
            color = colorUnspecified
            style = Paint.Style.STROKE
            strokeWidth = this@QualityProgressBar.strokeWidth
        }

        /* Draw arcs */
        arcs.forEach { arc ->
            val diff = sweepAngle - arc.startDeg
            if (diff > 0) {
                arc.sweepAngle = diff
                arc.draw(arcRect, rotation, canvas)
            }
        }

        val side = drawRect.right - drawRect.left + paddingEnd + paddingStart
        val x = drawRect.left - paddingStart
        val y = drawRect.top - paddingTop
        val radius = side / 2f

        if (text.isEmpty() || maxTextSize == 0f)
            return

        if (!calculatedTextSize)
            calculateTextSize(x, y, radius)

        canvas.drawText(text, x + radius - textBounds.width() / 2f, y + radius + textBounds.height() / 2f, textPaint)
    }

    private fun drawIdleStroke(canvas: Canvas) {
        val side = drawRect.right - drawRect.left + paddingEnd + paddingStart
        val x = drawRect.left - paddingStart
        val y = drawRect.top - paddingTop
        val radius = side / 2f

        val outerRadius = radius - idleStrokeWidth / 2f
        if (outerRadius < 0f)
            return

        canvas.drawCircle(x + radius, y + radius, outerRadius, idleStrokePaint)

        val innerRadius = outerRadius - strokeWidth + idleStrokeWidth
        if (innerRadius < 0f)
            return

        canvas.drawCircle(x + radius, y + radius, innerRadius, idleStrokePaint)
    }

    /**
     * Calculates size of text to perfect fit in progress circle
     * Stores result in [textBounds]
     *
     * @param x - x-center of progress circle
     * @param y - y-center of progress circle
     * @param targetWidth - target width (also height since its square) for text
     * */
    private fun calculateTextSize(x: Float, y: Float, targetWidth: Float) {
        var lastTextSize = 10000f
        textPaint.textSize = 1000f

        var diff: Float

        do {
            textPaint.getTextBounds(text, 0, text.length, textBounds)

            /* (x1, y1) - top-left corner of textBounds rect in canvas coordinates */
            val x1 = x + targetWidth / 2f - textBounds.width() / 2
            val y1 = y + targetWidth / 2f + textBounds.height() / 2
            /* (x2, y2) - right-bottom corner of textBounds rect in canvas coordinates */
            /* diff - the farthest distance between textBounds corners and progress circle */
            diff = calculateMaxRadius(x1, y1, x1 + textBounds.width(), y1 - textBounds.height(),
                    x + targetWidth / 2f, y + targetWidth / 2f) - (targetWidth - strokeWidth)

            /* Binary-search approach */
            if (diff > 0f) {
                lastTextSize = textPaint.textSize
                textPaint.textSize /= 2
            } else if (diff < -10f) {
                val current = textPaint.textSize
                textPaint.textSize += abs(lastTextSize - current) / 2f
                lastTextSize = current
            }
        } while (diff > 0f || diff < -10f)

        if (maxTextSize > 0 && textPaint.textSize > maxTextSize) {
            textPaint.textSize = maxTextSize
            textPaint.getTextBounds(text, 0, text.length, textBounds)
        }

        calculatedTextSize = true
    }

    private fun calculateMaxRadius(x1: Float, y1: Float, x2: Float, y2: Float, cx: Float, cy: Float): Float {
        var diff1 = (cx - x1).toDouble()
        var diff2 = (cy - y1).toDouble()

        val distance1 = Math.sqrt(diff1 * diff1 + diff2 * diff2)

        diff1 = (cx - x2).toDouble()
        diff2 = (cy - y2).toDouble()

        val distance2 = Math.sqrt(diff1 * diff1 + diff2 * diff2)

        return max(distance1, distance2).toFloat()
    }

    private fun updateArcRect() {
        arcRect.apply {
            set(drawRect)
            left += strokeWidth / 2f
            top += strokeWidth / 2f
            right -= strokeWidth / 2f
            bottom -= strokeWidth / 2f
        }
    }

    private data class ColoredArc(val fromDeg: Int, val toDeg: Int)

    private class Arc(val startDeg: Float, val maxAngle: Float, val paint: Paint, var color: Int) {
        var sweepAngle = 0f
            set(value) {
                field = when {
                    value < 0f -> 0f
                    value > maxAngle -> maxAngle
                    else -> value
                }
            }

        fun draw(rect: RectF, rotationOffset: Int, canvas: Canvas) {
            paint.color = color

            canvas.drawArc(rect, startDeg + rotationOffset, sweepAngle, false, paint)
        }
    }

    companion object {
        private val argbEvaluator = ArgbEvaluator()

        const val MAX_ARCS = 360
        const val ARC_STEP = MAX_ARCS / 360
    }
}