package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private class Corocr {
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable)
    }
    private val scope = CoroutineScope(SupervisorJob() + exceptionHandler)

    fun test() {
        scope.launch { // 1
            launch {
                delay(1000)
                println(" c 11")
            } // 11
            launch {
                delay(1000)
                println(" c 12")
            } // 12
            println(" c 1")
        }

        scope.launch { // 2
            launch {
                delay(1000)
                println(" c 21")
            } // 21
            launch {
                throw RuntimeException()
            } // 22
            println(" c 2")
            delay(500)
            println(" c 2 + 500")
        }
    }
}

fun main() {
    runBlocking {
        val co = Corocr()
        co.test()
        delay(2000)
        println(" done")
    }
}