package com.arvifox

import kotlin.math.absoluteValue

fun main() {
    println("start")
    var r = calc(listOf(1), 0, 1)
    println("res1 = $r")
    r = calc(listOf(1, 2, 4), 1, 2)
    println("res2 = $r")
    r = calc(listOf(1, 2, 5, 6, 8, 13, 14, 18), 3, 4)
    println("res3 = $r")
}

private fun calc(stations: List<Int>, index: Int, k: Int): List<Int> {
    if (k == 0) return emptyList()
    if (k == 1) return listOf(stations[index])
    val res = mutableListOf<Int>()
    var lIndex = if (index > 0) index - 1 else index
    var rIndex = if (index > 0) index else index + 1
    while (res.size < k) {
        val lDist = if (lIndex < 0) Int.MAX_VALUE else (stations[lIndex] - stations[index]).absoluteValue
        val rDist = if (rIndex > stations.lastIndex) Int.MAX_VALUE else (stations[rIndex] - stations[index]).absoluteValue
        if (lDist < rDist) {
            res.add(stations[lIndex])
            lIndex--
        } else {
            res.add(stations[rIndex])
            rIndex++
        }
    }

    return res
}
