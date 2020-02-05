package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object Corou018 {
    fun ooo1() {
        repeat(2) {
            ooo11()
        }
    }

    @Synchronized
    fun ooo11() {
        println("start")
        Thread.sleep(500)
        println("stop")
    }
}

class oufsdf {
    val scope = CoroutineScope(Job())
    fun sd() {
        repeat(2) {
            scope.launch { criticalSectionSuspending() }
        }
    }

    @Synchronized
    suspend fun criticalSectionSuspending() {
        println("Starting!")
        delay(10)
        println("Ending!")
    }
}

object nskv {
    val mutex = Mutex()
    val scope = CoroutineScope(Job())

    fun ssss() {
        repeat(2) {
            scope.launch { criticalSectionSuspendingLocked() }
        }
    }

    suspend fun criticalSectionSuspendingLocked() {
        mutex.withLock {
            println("Starting!")
            delay(10)
            println("Ending!")
        }
    }
}