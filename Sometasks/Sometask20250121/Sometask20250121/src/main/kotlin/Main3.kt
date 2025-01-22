package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    var depTime = reader.readLine().split(":").let {
        it[0].toInt() * 60 + it[1].toInt()
    }
    var arrTime = reader.readLine().split(":").let {
        it[0].toInt() * 60 + it[1].toInt()
    }

    val tz = reader.readLine().toInt()
    depTime += (tz * 60)
    while (depTime < 0) {
        depTime += 24 * 60
    }

    if (arrTime <= depTime) {
        arrTime += 24 * 60
    }

    val dur = arrTime - depTime
    val h = dur / 60
    val m = dur - h * 60
    writer.write("$h:${String.format("%02d", m)}")

    reader.close()
    writer.close()
}