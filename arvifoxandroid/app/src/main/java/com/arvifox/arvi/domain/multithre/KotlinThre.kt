@file:OptIn(ExperimentalUuidApi::class, ExperimentalStdlibApi::class)

package com.arvifox.arvi.domain.multithre

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.AtomicIntArray
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.plusAssign
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.asJavaAtomic
import kotlin.concurrent.atomics.asKotlinAtomic
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalAtomicApi::class)
object KotlinThre {
    private val atomicInt = AtomicInt(1)
    private val aaInt = AtomicIntArray(5) { 0 }
    private val aString = AtomicReference("")

    fun doint() {
        atomicInt += 1
    }

    private val javaAtomic = atomicInt.asJavaAtomic()
    private val kotlinAtomic = javaAtomic.asKotlinAtomic()
    fun comp() {
//        println(javaAtomic === kotlinAtomic) //true
    }

    private val kotlinUuid = Uuid.random()

    val hexFormatter = HexFormat {
        upperCase = true
        bytes {
            bytesPerGroup = 4
            groupSeparator = ":"
        }
    }
    fun uuu() {
        println(kotlinUuid.toString())
        println(kotlinUuid.toHexDashString())
        println(kotlinUuid.toHexString())
        println(kotlinUuid.toByteArray().toHexString(hexFormatter))
    }

    private val parsed = Uuid.parse("")
}
