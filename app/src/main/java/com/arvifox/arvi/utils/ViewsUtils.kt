package com.arvifox.arvi.utils

import android.text.Editable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.lang.Runnable

inline fun <reified T> Editable.removeSpans() {
    val allSpans = getSpans(0, length, T::class.java)
    for (span in allSpans) {
        removeSpan(span)
    }
}

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