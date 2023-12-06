package com.arvifox.arvi.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import com.arvifox.arvi.utils.ConvertUtils.dp
import kotlin.math.ceil

object ConvertUtils {
    fun Context.themeColor(@AttrRes a: Int) =
        obtainStyledAttributes(intArrayOf(a)).use { it.getColor(0, Color.RED) }

    val Int.dp: Int
        get() = ceil(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics
            )
        )
            .toInt()

    val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    val Fragment.dp: Int.() -> Float
        get() = {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                this@dp.resources.displayMetrics
            )
        }
}

class FooFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        val a = 345.dp()
    }
}