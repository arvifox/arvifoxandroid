package com.arvifox.arvi.kotlinpu

import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KotPu {

    @Test
    fun t001() {
        print(null.toString())
    }

    @Test
    fun t002() {
        for (i in 1..10 step 2 step 3) {
            print(" $i ")
        }
    }

    @Test
    fun t003() {
        suspend fun qwe(): String {
            return suspendCoroutine {
                it.resume("")
            }
        }
    }
}