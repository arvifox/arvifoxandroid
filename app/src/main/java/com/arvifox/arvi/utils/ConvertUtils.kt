package com.arvifox.arvi.utils

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.ceil

object ConvertUtils {
    val Int.dp: Int
        get() = ceil(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics))
            .toInt()

    val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)
}