package com.arvifox.arvi.domain

interface WithString {
    val s: String
}

interface WithInt {
    val i: Int
}

class Sti(
    override val s: String,
    override val i: Int,
): WithInt, WithString

class Inst(
    override val s: String,
    override val i: Int,
): WithInt, WithString

val stu = Sti(s = "qwe", i = 34,)
val qwe = Inst(s = "tyu", i = 87,)

private fun process(t: Any) {
    if (t is WithString && t is WithInt) {
        // t is intersection type
        println("${t.i} and ${t.s}")
    }
}

private fun <T> proccess(t: T) where T : WithInt, T : WithString {
    // t is intersection type
    println("${t.i} and ${t.s}")
}
