package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val str = reader.readLine().split(" ").map { it.toULong() }

    writer.write((str[0] + str[1]).toString())

    reader.close()
    writer.close()
}
