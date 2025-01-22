package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s1 = reader.readLine().toInt()
    val s2 = reader.readLine().toInt()
    val s3 = reader.readLine().toInt()

    if ((s1 + s2 > s3) && (s1 + s3 > s2) && (s2 + s3 > s1)) {
        writer.write("YES")
    } else {
        writer.write("NO")
    }

    reader.close()
    writer.close()
}
