package com.arvifox.arvi.domain.corou

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Semaphore

fun <T> runOnBackgroundThread(backgroundFunc: () -> T, callback: (T) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    Thread {
        val result = backgroundFunc()
        handler.post { callback(result) }
    }.start()
}

object Arv06 {
    class Incrementor() {
        private val sharedCounterLock = Semaphore(1)
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                try {
                    sharedCounterLock.acquire()
                    sharedCounter++
                } finally {
                    sharedCounterLock.release()
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

object Arv07 {
    fun main() {
        val createdValues = mutableListOf<Int>()
        /**
         *  create a cyclic barrier that waits for 3 threads to finish their jobs, and after that,
         *  prints the sum of all the values.
         */
        val cyclicBarrier = CyclicBarrier(3) {
            println("Sum of all values is ${createdValues.sum()}")
        }

        val threads = 1.rangeTo(3).map { number ->
            Thread {
                Thread.sleep(Random().nextInt(500).toLong())
                createdValues.add(number)
                cyclicBarrier.await()
                println("I am thread ${Thread.currentThread().name} and I finished my Job!")
            }.apply {
                start()
            }
        }
        threads.forEach { thread ->
            thread.join()
        }

    }
}