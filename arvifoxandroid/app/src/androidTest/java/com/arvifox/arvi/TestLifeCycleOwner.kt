package com.arvifox.arvi

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * A [LifecycleOwner] which is always in a [Lifecycle.State.STARTED] state.
 */
class TestLifeCycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this)

    init {
        registry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle = registry
}
