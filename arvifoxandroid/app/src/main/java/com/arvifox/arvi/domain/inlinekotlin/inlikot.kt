package com.arvifox.arvi.domain.inlinekotlin

/**
 * [https://www.baeldung.com/kotlin/inline-functions]
 */
fun <T> Collection<T>.filter(predicate: (T) -> Boolean): Collection<T> {
    return emptyList()
}

//fun <T> Collection<T>.each(block: (T) -> Unit) {
//    for (e in this) block(e)
//}

inline fun <T> Collection<T>.each(block: (T) -> Unit) {
    for (e in this) block(e)
}

inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {

}

inline fun <reified T> Any.isA(): Boolean = this is T

fun <T> List<T>.eachIndexed(f: (Int, T) -> Unit) {
    for (i in indices) {
        f(i, this[i])
    }
}

inline fun <T> List<T>.eachIndexed2(f: (Int, T) -> Unit) {
    for (i in indices) {
        f(i, this[i])
    }
}

inline fun <T> List<T>.eachIndexed3(crossinline f: (Int, T) -> Unit) {
    for (i in indices) {
        f(i, this[i])
    }
}

/**
 * try all options of _eachIndexed*_
 */
fun <T> List<T>.indexOf(x: T): Int {
    eachIndexed2 { index, value ->
        if (value == x) {
            return index
        }
    }
    return -1
}
