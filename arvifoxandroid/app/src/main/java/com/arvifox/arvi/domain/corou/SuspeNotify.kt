package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@JvmInline
value class Waiter(private val channel: Channel<Unit> = Channel<Unit>(0)) {

    suspend fun doWait() { channel.receive() }
    fun doNotify() {
        channel.trySend(Unit).isSuccess
    }
}

class SuspendWait() {
    private var myCont: Continuation<Unit>? = null
    suspend fun sleepAndWait() = suspendCoroutine<Unit>{ cont ->
        myCont = cont
    }

    fun resume() {
        val cont = myCont
        myCont = null
        cont?.resume(Unit)
    }
}

class SuspendWait2 {
    private val mutex = Mutex(locked = true)
    suspend fun sleepAndWait() = mutex.withLock{}
    fun resume() {
        mutex.unlock()
    }
}
