package com.arvifox.arvi.domain.interview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class AspectRatioFrameLayout : ViewGroup {
    // Конструктор
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Получаем размеры из спецификации
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        // Рассчитываем размеры с учетом соотношения 4:3
        var desiredWidth = widthSize
        var desiredHeight = (desiredWidth * 4.0f / 3.0f).toInt()

        // Если высота задана точно, пересчитываем ширину
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
            desiredWidth = (desiredHeight * 3.0f / 4.0f).toInt()
        }

        // Устанавливаем измеренные размеры
        setMeasuredDimension(desiredWidth, desiredHeight)

        // Измеряем дочерние элементы
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Располагаем все дочерние элементы по центру
        val count = childCount
        for (i in 0..<count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                val left = (width - childWidth) / 2
                val top = (height - childHeight) / 2

                child.layout(left, top, left + childWidth, top + childHeight)
            }
        }
    }
}