package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val str = reader.readLine().split(" ").let { ls ->
        IntArray(ls.size) { ls[it].toInt() }
    }

    writer.write(do2(str).joinToString(" "))

    reader.close()
    writer.close()
}

private fun do2(nu: IntArray): IntArray {
    var cu = 0
    var ze = -1
    var zec = 0
    while (cu < nu.size) {
        if (nu[cu] == 0) {
            zec++
            if (ze == -1) {
                ze = cu
            }
        } else {
            if (ze != -1) {
                nu[ze] = nu[cu]
                ze++
            }
        }
        cu++
    }
    repeat(zec) {
        nu[--cu] = 0
    }
    return nu
}

private fun do1(nu: IntArray): IntArray {
    var la = nu.lastIndex
    var cu = 0
    while (cu < la) {
        if (nu[cu] == 0) {
            var id = cu
            while (id < la) {
                nu[id] = nu[id + 1]
                id++
            }
            nu[la] = 0
            la--
        }
        if (nu[cu] != 0) cu++
    }
    return nu
}
