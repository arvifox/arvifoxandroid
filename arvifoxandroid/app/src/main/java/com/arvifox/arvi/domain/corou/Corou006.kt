package com.arvifox.arvi.domain.corou

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

object Arv08 {

    fun ar01() {
        thread {
            println("SAM thread start")
        }
    }

    private val myService: ExecutorService = Executors.newFixedThreadPool(8)
    var i = 0
    private var items = mutableListOf<Int>()

    fun ar02() {
        while (i < items.size) {
            val item = items[i]
            myService.submit {
                processItem(item) // a long running operation
            }
            i += 1
        }
    }

    private fun processItem(i: Int) {

    }
}

object Arv09 {
    fun ad() {
        val myService: ExecutorService = Executors.newFixedThreadPool(2)
        val result = myService.submit(Callable<String> {
            return@Callable ""
        })
        println(result.get())
    }
}