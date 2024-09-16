package com.arvifox.arvi.domain.multithre

interface P {
    val x: Int
}

fun printMe(me: P) = println(me)

fun printMeInLambda(lambda: () -> P) = printMe(lambda())

fun printMeInThread(lambda: () -> P) = kotlin.concurrent.thread { printMeInLambda(lambda) }

abstract class AbstractP : P {
    override fun toString() = "?"
    init {
        printMe(this)
        printMeInLambda { this }
        printMeInThread { this }
    }
}

class PpImpl(override val x: Int) : AbstractP() {
    override fun toString() = x.toString()
}

fun letsStart() {
    PpImpl(5)
}
