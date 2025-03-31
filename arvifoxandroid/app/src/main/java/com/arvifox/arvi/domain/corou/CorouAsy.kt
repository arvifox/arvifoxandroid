package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue

fun main() = runBlocking {
    val df = async {
        Co().doe(12)
    }
    try {
        val re = df.await()
        println("re=$re")
    } catch (t: Throwable) {
        println("catch err [${t.localizedMessage}]")
    }
    println("done")
}

private class Co {
    fun doe(a: Int): Int {
        return if (a.absoluteValue > 10) {
            throw RuntimeException("bybysh")
        } else {
            println("a=$a")
            a
        }
    }
}
