package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

object Arv03 {
    class Incrementor() {
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                synchronized(this) {
                    sharedCounter++
                }
            }
        }
    }

    fun main() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                launch {
                    for (i in 1..1000) {
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }
            coroutines.forEach { corotuine ->
                corotuine.join()
            }
        }.join()
        println("The number of shared counter is ${incrementor.sharedCounter}")
    }
}

object Arv04 {
    class Incrementor() {
        val sharedCounter: AtomicInteger = AtomicInteger(0)

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                sharedCounter.incrementAndGet()
            }
        }

        fun getSharedCounter(): Int {
            return sharedCounter.get()
        }
    }

    fun main() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                launch {
                    for (i in 1..1000) {
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }

            coroutines.forEach { corotuine ->
                corotuine.join()
            }
        }.join()

        println("The number of shared counter is ${incrementor.getSharedCounter()}")
    }
}