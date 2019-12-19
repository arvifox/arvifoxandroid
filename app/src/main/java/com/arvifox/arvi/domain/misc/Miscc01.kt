package com.arvifox.arvi.domain.misc

inline class Color(private val c: Int) {
    val a get() = (c shr 24) and 0xff
    val r get() = (c shr 16) and 0xff
    val g get() = (c shr 8) and 0xff
    val b get() = (c) and 0xff
}

object Miscc01 {
    fun printcolor(color: Color) {
        println(
            """
            red   = ${color.r}
            green = ${color.g}
            blue  = ${color.b}
            alpha = ${color.a}
        """.trimIndent()
        )
    }

    fun fff() {
        val co = Color(0x7f_10_20_30)
        printcolor(co)
    }
}