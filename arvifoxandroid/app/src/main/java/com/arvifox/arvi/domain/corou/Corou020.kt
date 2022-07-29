package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

// https://medium.com/@davidecerbo/backpressure-in-kotlin-flows-9324d86c964e

object Corou020 {

    fun currTime() = System.currentTimeMillis()
    fun threadName() = Thread.currentThread().name
    var start: Long = 0

    fun emitter(): Flow<Int> =
        (1..5)
            .asFlow()
            .onStart { start = currTime() }
            .onEach {
                delay(1000)
                print("Emit $it (${currTime() - start}ms) ")
            }

    @InternalCoroutinesApi
    fun main() = runBlocking<Unit> {
        val time = measureTimeMillis {
            emitter()
                .collect {
                    print("\nCollect $it starts (${currTime() - start}ms) ")
                    delay(3000)
                    println("Collect $it ends (${currTime() - start}ms) ")
                }
        }
        print("\nCollected in $time ms")
    }

    @InternalCoroutinesApi
    fun main2() = runBlocking<Unit> {
        val time = measureTimeMillis {
            emitter()
                .flowOn(Dispatchers.Default)
                .collect {
                    print("\nCollect $it starts (${currTime() - start}ms) ")
                    delay(3000)
                    println("Collect $it ends (${currTime() - start}ms) ")
                }
        }
        print("\nCollected in $time ms")
    }

    @InternalCoroutinesApi
    fun main3() = runBlocking<Unit> {
        val time = measureTimeMillis {
            emitter()
                .buffer()
                .collect {
                    print("\nCollect $it starts (${currTime() - start}ms) ")
                    delay(3000)
                    println("Collect $it ends (${currTime() - start}ms) ")
                }
        }
        print("\nCollected in $time ms")
    }

    @InternalCoroutinesApi
    fun main4() = runBlocking<Unit> {
        val time = measureTimeMillis {
            emitter()
                .conflate()
                .collect {
                    print("\nCollect $it starts (${currTime() - start}ms) ")
                    delay(3000)
                    println("Collect $it ends (${currTime() - start}ms) ")
                }
        }
        print("\nCollected in $time ms")
    }
}