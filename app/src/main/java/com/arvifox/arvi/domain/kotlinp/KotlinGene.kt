package com.arvifox.arvi.domain.kotlinp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * [https://www.baeldung.com/kotlin/generics]
 */
class ParameterizedClass<A>(private val value: A) {

    fun getValue(): A {
        return value
    }
}

object KotlinGene {
    fun te1() {
        val parameterizedClass = ParameterizedClass("string-value")
        val res = parameterizedClass.getValue()
        assertTrue(res is String)
    }

    fun te2() {
        val parameterizedProducer = ParameterizedProducer("string")
        val ref: ParameterizedProducer<Any> = parameterizedProducer
        assertTrue(ref is ParameterizedProducer<Any>)
    }

    fun te3() {
        val parameterizedConsumer = ParameterizedConsumer<Number>()
        val ref: ParameterizedConsumer<Double> = parameterizedConsumer
        assertTrue(ref is ParameterizedConsumer<Double>)
    }

    fun te4() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any: Array<Any?> = arrayOfNulls(3)

        copy(ints, any)

        assertEquals(any[0], 1)
        assertEquals(any[1], 2)
        assertEquals(any[2], 3)
    }
}

/**
 * we need to use the _out_ keyword on the generic type.
 * It means that we can assign this reference to any of its supertypes.
 * The out value can be only be produced by the given class but not consumed:
 */
class ParameterizedProducer<out T>(private val value: T) {
    fun get(): T {
        return value
    }
}

/**
 * We can use the _in_ keyword on the generic type
 * if we want to assign it to the reference of its subtype.
 * The in keyword can be used only on the parameter type
 * that is consumed, not produced:
 */
class ParameterizedConsumer<in T> {
    fun toString(value: T): String {
        return value.toString()
    }
}

fun copy(from: Array<out Any>, to: Array<Any?>) {
    assert(from.size == to.size)
    for (i in from.indices)
        to[i] = from[i]
}

fun <T> sort(xs: List<T>) where T : CharSequence, T : Comparable<T> {
    // sort the collection in place
}

fun <T: Comparable<T>> sort(list: List<T>): List<T> {
    return list.sorted()
}

//fun <T> Iterable<*>.filterIsInstance() = filter { it is T }

inline fun <reified T> Iterable<*>.filterIsInstance() = filter { it is T }