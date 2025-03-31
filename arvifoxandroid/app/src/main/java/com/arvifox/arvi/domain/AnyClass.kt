package com.arvifox.arvi.domain

interface Processor<T> {
    fun process(): T
}

fun FooJavaClass.extfoo(): Int {
    return this.g * 2
}

fun SomeChe.po(): Int {
    return this.che() + 3
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
