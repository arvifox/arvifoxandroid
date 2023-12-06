package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

object Arv02 {

    var sharedCounter = 0
    @Synchronized
    fun updateCounter() {
        sharedCounter++
    }

    fun main() = runBlocking {
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                launch {
                    for (i in 1..1000) {
                        updateCounter()
                    }
                }
            }

            coroutines.forEach { corotuine ->
                corotuine.join()
            }
        }.join()

        println("The number of shared counter is $sharedCounter")
    }
}