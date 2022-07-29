package com.arvifox.arvi.domain

import kotlinx.coroutines.flow.*

fun fib1(a: Int = 0, b: Int = 1): Flow<Int> = flow {
    emit(a)
    emitAll(fib1(b, a + b))
}

fun fib2(): Sequence<Int> = generateSequence(0 to 1) { (a, b) ->
    b to a + b
}.map { it.first }

fun fib3(): Sequence<Int> = generateSequence(IntPair(0, 1)) { (a, b) ->
    IntPair(b, a + b)
}.map { it.a }

fun fib4(n: Int): Int {
    tailrec fun fibb(n: Int, a: Int, b: Int): Int = if (n == 0) a else fibb(n - 1, b, a + b)
    return fibb(n, a = 0, b = 1)
}

@JvmInline
value class IntPair private constructor(private val value: Long) {
    constructor(a: Int, b: Int) : this(value = a.toLong().shl(32) or (b.toLong() and 0xffffffffL))

    val a: Int get() = value.shr(32).toInt()
    val b: Int get() = value.toInt()
    operator fun component1(): Int = a
    operator fun component2(): Int = b
}

suspend fun main() {
    fib1().take(10).collect { print("$it") }
    fib2().take(10).forEach { print("$it") }
}