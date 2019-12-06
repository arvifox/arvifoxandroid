package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.delay
import java.io.IOException

object Arvi_re {

    suspend fun <T> retry(block: suspend () -> T): T {
        var cur = 1000L
        while (true) {
            try {
                return block()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            delay(cur)
            cur = (cur * 2).coerceAtMost(60000L)
        }
    }
}