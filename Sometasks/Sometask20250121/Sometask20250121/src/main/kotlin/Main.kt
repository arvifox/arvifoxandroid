package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    println("Hello World!")
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val count = reader.readLine().toInt()
    val inputString = reader.readLine()
    val millis = reader.readLine().split(" ").map { it.toInt() }

    var maxMil = millis[0]
    var maxInd = 0

    var ind = 1
    while (ind < count) {
        if (millis[ind] - millis[ind - 1] >= maxMil) {
            maxMil = millis[ind] - millis[ind - 1]
            maxInd = ind
        }
        ind++
    }

    writer.write(inputString[maxInd].toString())

    reader.close()
    writer.close()
}