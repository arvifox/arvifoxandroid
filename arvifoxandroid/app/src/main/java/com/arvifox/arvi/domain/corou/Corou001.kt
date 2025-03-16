package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.startCoroutine

suspend fun doAsync(a: Int) {
    println(a)
}

fun callDoAsync() {
    ::doAsync.startCoroutine(7, object : Continuation<Unit> {
        override val context: CoroutineContext
            get() = TODO("Not yet implemented")

        override fun resumeWith(result: Result<Unit>) {
            TODO("Not yet implemented")
        }
    })
}

object Arv01 {
    @Volatile
    var fooCounter: Int = 0

    fun main() =
        runBlocking {
            var sharedCounter = 0
            val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
            scope.launch {
                val coroutines =
                    1.rangeTo(1000).map {
                        launch {
                            for (i in 1..1000) {
                                sharedCounter++
                            }
                        }
                    }
                coroutines.forEach { corotuine ->
                    corotuine.join()
                }
            }.join()

            println("The number of shared counter should be 10000000, but actually is $sharedCounter")
        }
}
