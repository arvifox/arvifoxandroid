package com.arvifox.arvi.domain.texts

object TextsUtils {
    private const val smu = "asd☺jkl"

    fun check() {
        val ba = smu.toByteArray()
        val f: Char = smu.first()
        println("smu l ${smu.length} s ${ba.size}")
        val d = 76
        println("[$ba]")
        smu.forEach {
            println("sy $it, ${it.code}-${it.code.toString(16)}, ${it.isSurrogate()}, ${it.isLetter()}")
        }
        val sbb = buildString {
            append('\u0024')
            append('\u263a')
        }
        println("sb = $sbb")
    }
}
