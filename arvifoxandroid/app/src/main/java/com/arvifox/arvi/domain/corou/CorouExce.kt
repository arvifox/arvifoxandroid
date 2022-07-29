package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.*

/**
 * [https://www.lukaslechner.com/why-exception-handling-with-kotlin-coroutines-is-so-hard-and-how-to-successfully-master-it/]
 */
object CorouExce {

    // ok
    fun main() {
        val topLevelScope = CoroutineScope(Job())
        topLevelScope.launch {
            try {
                throw RuntimeException("RuntimeException in coroutine")
            } catch (exception: Exception) {
                println("Handle $exception")
            }
        }
        Thread.sleep(100)
    }

    // exception occurs
    fun main2() {
        val topLevelScope = CoroutineScope(Job())
        topLevelScope.launch {
            try {
                launch {
                    throw RuntimeException("RuntimeException in nested coroutine")
                }
            } catch (exception: Exception) {
                println("Handle $exception")
            }
        }
        Thread.sleep(100)
    }

    // CoroutineExceptionHandler, exception occurs
    fun main3() {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(Job())

        topLevelScope.launch {
            launch(coroutineExceptionHandler) {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        }

        Thread.sleep(100)
    }

    // CoroutineExceptionHandler, ok
    /*
    In order for a CoroutineExceptionHandler to have an effect, it must be installed either in the CoroutineScope or in a top-level coroutine.
     */
    fun main4() {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(Job() + coroutineExceptionHandler)

        topLevelScope.launch {
            launch {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        }

        Thread.sleep(100)
    }

    // no any output
    fun main5() {

        val topLevelScope = CoroutineScope(SupervisorJob())

        val f = topLevelScope.async {
            println("tratata tratata")
            throw RuntimeException("RuntimeException in async coroutine")
        }

        Thread.sleep(100)
    }

    // ok, exception is caught
    fun main6() {

        val topLevelScope = CoroutineScope(SupervisorJob())

        val deferredResult = topLevelScope.async {
            println("tratata tratata")
            throw RuntimeException("RuntimeException in async coroutine")
        }

        topLevelScope.launch {
            try {
                deferredResult.await()
            } catch (exception: Exception) {
                println("Handle $exception in try/catch")
            }
        }

        Thread.sleep(100)
    }

    // exception occurs
    fun main7() {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)
        topLevelScope.launch {
            async {
                throw RuntimeException("RuntimeException in async coroutine")
            }
        }
        Thread.sleep(100)
    }

    // exception is caught in catch
    fun main8() {

        val topLevelScope = CoroutineScope(Job())

        topLevelScope.launch {
            try {
                coroutineScope {
                    launch {
                        throw RuntimeException("RuntimeException in nested coroutine")
                    }
                }
            } catch (exception: Exception) {
                println("Handle $exception in try/catch")
            }
        }

        Thread.sleep(100)
    }

    //
    fun main9() {

        val topLevelScope = CoroutineScope(Job())

        topLevelScope.launch {
            val job1 = launch {
                println("starting Coroutine 1")
            }

            supervisorScope {
                val job2 = launch {
                    println("starting Coroutine 2")
                }

                val job3 = launch {
                    println("starting Coroutine 3")
                }
            }
        }

        Thread.sleep(100)
    }

    //
    fun main10() {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(Job())

        topLevelScope.launch {
            val job1 = launch {
                println("starting Coroutine 1")
            }

            supervisorScope {
                val job2 = launch(coroutineExceptionHandler) {
                    println("starting Coroutine 2")
                    throw RuntimeException("Exception in Coroutine 2")
                }

                val job3 = launch {
                    println("starting Coroutine 3")
                }
            }
        }

        Thread.sleep(100)
    }
}