package com.arvifox

import kotlin.math.max

fun main() {
    println("c=${calc("aaabcdaaytroiaam")}")
    println("c=${calc("abcde")}")
}

private fun calc(s: String): Int {
    if (s.isEmpty()) return 0
    var max: Int = 0
    var le: Int = 0
    val col = mutableMapOf<Char, Int>()
    for (ri in 0..s.lastIndex) {
        val cur = col.getOrDefault(s[ri], 0)
        col[s[ri]] = cur + 1
        if (col[s[ri]]!! > 1) {
            while (col[s[ri]]!! > 1) {
                col[s[le]] = col[s[le]]!! - 1
                le++
            }
        }
        max = max(max, ri - le + 1)
        println("s: $max; ${s.substring(le, ri+1)}")
    }

    return max
}