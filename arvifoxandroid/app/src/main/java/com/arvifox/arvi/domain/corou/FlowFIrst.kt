package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Вызывает [block] на первом элементе в цепочке
 */
fun <T> Flow<T>.onFirst(block: suspend (T) -> Unit): Flow<T> {
    val isFirstMet = AtomicBoolean(false)
    return this.onEach { value ->
        if (!isFirstMet.getAndSet(true)) {
            block(value)
        }
    }
}

/**
 * Вызывает [block] на первом не-null элементе в цепочке
 */
fun <T : Any> Flow<T?>.onFirstNonNull(block: suspend (T) -> Unit): Flow<T?> {
    val isFirstMet = AtomicBoolean(false)
    return this.onEach { value ->
        if (value != null) {
            if (!isFirstMet.getAndSet(true)) {
                block(value)
            }
        }
    }
}

/**
 * Вызывает [block] на первом элементе удовлетворяющим [predicate]
 */
fun <T : Any> Flow<T>.onFirstIf(
    predicate: suspend (T) -> Boolean,
    block: suspend (T) -> Unit,
): Flow<T> {
    val isFirstMet = AtomicBoolean(false)
    return this.onEach { value ->
        if (predicate(value)) {
            if (!isFirstMet.getAndSet(true)) {
                block(value)
            }
        }
    }
}