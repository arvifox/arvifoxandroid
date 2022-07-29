package com.arvifox.arvi.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

fun <T> Flow<Event<T?>>.onEachEvent(action: suspend (T) -> Unit): Flow<T> = transform { value ->
    value.getContentIfNotHandled()?.let {
        action(it)
        return@transform emit(it)
    }
}