package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.*

/**
 * [https://www.youtube.com/watch?v=w0kfnydnFWI]
 */
object CoroutExce {
    fun qwer01() {
        val startTime = System.currentTimeMillis()
        val job = CoroutineScope(Dispatchers.Default).launch {
            var next = startTime
            var i = 0
            while (i<5 && isActive) {
                ensureActive()
                yield()
                if (System.currentTimeMillis() >= next) {
                    println("Hello ${i++}")
                    next += 500L
                }
            }
        }
        println("before sleep")
        Thread.sleep(1000L)
        println("cancel")
        job.cancel()
        println("done")
    }

    fun qwer02() {
        val startTime = System.currentTimeMillis()
        val job = CoroutineScope(Dispatchers.Default).launch {
            var next = startTime
            var i = 0
            repeat(5) {
                println("${i++}")
                delay(500L)
            }
        }
        println("before sleep")
        Thread.sleep(1000L)
        println("cancel")
        job.cancel()
        println("done")
    }
}