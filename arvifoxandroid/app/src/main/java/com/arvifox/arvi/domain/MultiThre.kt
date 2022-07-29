package com.arvifox.arvi.domain

import java.util.concurrent.Executors

object MultiThre {
    fun dodoit() {
        val counter = Counter(0)
        val runnable1 = Runnable {
            for (i in 0..999) {
                counter.increment()
            }
        }
        val runnable2 = Runnable {
            for (i in 0..999) {
                counter.decrement()
            }
        }
        val executors = Executors.newCachedThreadPool()
        executors.execute(runnable1)
        executors.execute(runnable2)
    }
}

class Counter(var count: Long) {
    fun increment() {
        count++
    }
    fun decrement() {
        count--
    }
}
