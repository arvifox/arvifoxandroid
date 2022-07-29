package com.arvifox.arvi.utils

import android.text.Editable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.lang.Runnable

fun sdf(v: View) {
    v.setOnLongClickListener {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            while (it.isPressed) {
                // click in progress
            }
        }
        true
    }
}

inline fun <reified T> Editable.removeSpans() {
    val allSpans = getSpans(0, length, T::class.java)
    for (span in allSpans) {
        removeSpan(span)
    }
}

fun Float.dpToPx(dp: DisplayMetrics): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, dp).toInt()

fun View.dip2px(dip: Int): Float = dip * resources.displayMetrics.density
fun View.dip2px(dip: Float): Float = dip2px(dip.toInt())

fun View.postDelayedSafe(delayMillis: Long, block: () -> Unit) {
    val runnable = Runnable { block() }
    postDelayed(runnable, delayMillis)
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) {}

        override fun onViewDetachedFromWindow(view: View) {
            removeOnAttachStateChangeListener(this)
            view.removeCallbacks(runnable)
        }
    })
}

class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes), CoroutineScope by MainScope() {

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext[Job]?.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

fun BaseFragment.delayActionSafe(delayMillis: Long, action: () -> Unit): Job? {
    view ?: return null
    return launch {
        delay(delayMillis)
        action()
    }
}