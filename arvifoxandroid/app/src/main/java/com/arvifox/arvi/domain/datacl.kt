package com.arvifox.arvi.domain

data class Cat(val name: String = "catte")

fun er() {
    val c1 = Cat()
    val c2 = Cat()
    val ccc = mutableSetOf(c1)
    val a1 = ccc.contains(c1)
    val a2 = ccc.contains(c2)
}