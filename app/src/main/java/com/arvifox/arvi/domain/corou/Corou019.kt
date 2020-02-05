package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// https://medium.com/@elizarov/callbacks-and-kotlin-flows-2b53aa2525cf

object Corou019 {

    interface Operation<T> {
        fun performAsync(callback: (T?, Throwable?) -> Unit)
        fun cancel() // cancels ongoing operation
    }

    fun <T : Any> Operation<T>.perform(): Flow<T> =
        callbackFlow {
            performAsync { value, exception ->
                when {
                    exception != null -> // operation had failed
                        close(exception)
                    value == null -> // operation had succeeded
                        close()
                    else -> // there is a value
//                        offer(value as T)
                        sendBlocking(value as T)
                }
            }
            awaitClose { cancel() }
        }
}