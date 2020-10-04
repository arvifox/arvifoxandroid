package com.arvifox.arvi.domain.corou

import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * [https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.concurrent/]
 */
object Corou022 {

    fun fun01() {
        Thread { println("some") }.start()
    }

    fun fun02() {
        thread(start = true) { println("some") }
    }

    @Synchronized
    fun fun03() {
        println("")
    }

    fun fun04() {
        synchronized(this) {
            println("")
        }
    }

    @Volatile
    private var asd = false

    private val lock = java.lang.Object()

    private var items: Int = 0
    private var maxItems: Int = 0

    fun produce() = synchronized(lock) {
        while (items >= maxItems) {
            lock.wait()
        }
        Thread.sleep(Random.nextInt(100).toLong())
        items++
        println("Produced, count is $items: ${Thread.currentThread()}")
        lock.notifyAll()
    }

    fun consume() = synchronized(lock) {
        while (items <= 0) {
            lock.wait()
        }
        Thread.sleep(Random.nextInt(100).toLong())
        items--
        println("Consumed, count is $items: ${Thread.currentThread()}")
        lock.notifyAll()
    }
}