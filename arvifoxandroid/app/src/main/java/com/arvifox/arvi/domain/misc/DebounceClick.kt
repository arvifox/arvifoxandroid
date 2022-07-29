package com.arvifox.arvi.domain.misc

import android.view.View

abstract class DebouncingClickListener : View.OnClickListener {
    private var enabled: Boolean = true
    private val enable: () -> Unit = { enabled = true }
    override fun onClick(p0: View?) {
        if (enabled) {
            enabled = false
            p0?.post { enable.invoke() }
            click(p0)
        }
    }

    abstract fun click(v: View?)
}