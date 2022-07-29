package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock

object Arv05 {
    class Incrementor() {
        private val sharedCounterLock = ReentrantLock()
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                try {
                    sharedCounterLock.lock()
                    sharedCounter++
                } finally {
                    sharedCounterLock.unlock()
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