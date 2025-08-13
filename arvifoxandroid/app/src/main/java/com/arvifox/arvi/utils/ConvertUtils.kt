package com.arvifox.arvi.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.arvifox.arvi.utils.ConvertUtils.dpxx
import kotlin.math.ceil

object ConvertUtils {
    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.themeColor(
        @AttrRes a: Int,
    ) = obtainStyledAttributes(intArrayOf(a)).use { it.getColor(0, Color.RED) }

    val Int.dpxx: Int
        get() =
            ceil(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    this.toFloat(),
                    Resources.getSystem().displayMetrics,
                ),
            )
                .toInt()

    val Float.dpxx: Float
        get() =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                Resources.getSystem().displayMetrics,
            )

    val Fragment.dpxx: Int.() -> Float
        get() = {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                this@dpxx.resources.displayMetrics,
            )
        }
}

class FooFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        val a = 345.dpxx()
    }
}
