package com.arvifox.arvi.domain

interface Processor<T> {
    fun process(): T
}

class NoResultProcessor : Processor<Unit> {
    override fun process() {
        // do stuff
    }
}

fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

class AnyClass : Any() {

    override fun toString(): String {
        return "AnyClass()"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}