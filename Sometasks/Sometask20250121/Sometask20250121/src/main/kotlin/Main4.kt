package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val exi = mutableSetOf<Int>()
    var su: ULong = 0u

    val n = reader.readLine().toInt()

    reader.readLine().split(" ").forEachIndexed { _, s ->
        val va = s.toInt()
        if (exi.contains(va).not()) {
            su += va.toUInt()
            exi.add(va)
        }
    }

    writer.write(su.toString())

    reader.close()
    writer.close()
}