package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val count = reader.readLine().toInt()
    val coms = arrayOfNulls<Char>(count)
    val lettersCount = HashMap<Char, Int>()
    var maxLetter: Char = ' '
    var maxCount: Int = 0
    for (i in 0 until count) {
        coms[i] = reader.readLine()[0]
        val curCount = lettersCount.getOrPut(coms[i]!!) { 0 }
        lettersCount[coms[i]!!] = curCount + 1
        if (curCount + 1 > maxCount) {
            maxCount = curCount + 1
            maxLetter = coms[i]!!
        }
    }

    writer.write(maxLetter.toString())

    reader.close()
    writer.close()
}
