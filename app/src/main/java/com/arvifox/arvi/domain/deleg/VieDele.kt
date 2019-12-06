package com.arvifox.arvi.domain.deleg

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.custom_view.view.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object sdf {
    fun TextView.text(): ReadWriteProperty<Any, String> =
        object : ReadWriteProperty<Any, String> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ): String = text.toString()

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>, value: String
            ) {
                text = value
            }
        }

    class CustomView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : FrameLayout(context, attrs) {

        init {
            inflate(context, R.layout.custom_view, this)
        }

        var title by tvTitle.text()
        var subtitle by tvSubtitle.text()
        var description by tvDescription.text()
    }

    fun View.isVisible(keepBounds: Boolean): ReadWriteProperty<Any, Boolean> =
        object : ReadWriteProperty<Any, Boolean> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ): Boolean = visibility == View.VISIBLE

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Boolean
            ) {
                visibility = when {
                    value -> View.VISIBLE
                    keepBounds -> View.INVISIBLE
                    else -> View.GONE
                }
            }
        }

    fun ProgressBar.progress(): ReadWriteProperty<Any, Float> =
        object : ReadWriteProperty<Any, Float> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ): Float = if (max == 0) 0f else progress / max.toFloat()

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>, value: Float
            ) {
                progress = (value * max).toInt()
            }
        }
}