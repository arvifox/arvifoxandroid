package com.arvifox

import kotlin.math.max

fun main() {
    val rere = Rere()
    val ss = rere.readRes("/interseinterva.txt")
    ss.forEachIndexed { index, va ->
        val array = toIn(va)
        println("data $index: ${array.pri()}")
        val r = calc(array)
        println("res = $r")
        println("-------")
    }
}

private fun calc(a: Array<Inte>): String {
    if (a.isEmpty()) return "the same"
    a.sortBy { it.a }
    val rl = mutableListOf<Inte>()
    rl.add(Inte(a[0].a, a[0].b))
    for (i in 1..a.lastIndex) {
        val last = rl.last()
        if (last.b >= a[i].a) {
            last.b = max(last.b, a[i].b)
        } else {
            rl.add(a[i])
        }
    }

    return rl.toTypedArray().pri()
}

private data class Inte(var a: Int, var b: Int)

private fun Array<Inte>.pri(): String = buildString {
    this@pri.forEach { ia: Inte ->
        this.append(ia.a)
        this.append(" ")
        this.append(ia.b)
        this.append("; ")
    }
}

private fun toIn(s: String): Array<Inte> {
    if (s.isEmpty()) return emptyArray()
    val ints = s.split(":")
    val res = Array(ints.size) { Inte(0, 0) }
    ints.forEachIndexed { index, si ->
        val its = si.split(" ").map { it.toInt() }
        res[index].a = its[0]
        res[index].b = its[1]
    }
    return res
}
