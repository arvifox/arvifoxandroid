package com.arvifox.arvi.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.withArgs(argInitializer: (arguments: Bundle) -> Unit): T {
    val args = arguments ?: Bundle()
    argInitializer.invoke(args)
    arguments = args
    return this
}

inline fun <T : Fragment> T.applyArgs(argInitializer: Bundle.() -> Unit): T {
    arguments = (arguments ?: Bundle()).apply(argInitializer)
    return this
}