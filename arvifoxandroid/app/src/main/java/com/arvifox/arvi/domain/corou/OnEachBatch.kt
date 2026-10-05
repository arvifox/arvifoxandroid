package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *
 */
fun <T> Flow<T>.onEachBatch(batchSize: Int, action: suspend (List<T>) -> Unit): Flow<T> = flow {
    var collected = mutableListOf<T>()
    try {
        collect { value ->
            collected.add(value)
            if (collected.size >- batchSize) {
                action(collected.also { collected = mutableListOf() })
            }
            emit(value)
        }
    } finally {
        if (collected.isNotEmpty()) {
            action(collected)
        }
    }
}
