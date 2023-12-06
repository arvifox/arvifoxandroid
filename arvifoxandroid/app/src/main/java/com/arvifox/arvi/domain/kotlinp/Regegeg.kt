package com.arvifox.arvi.domain.kotlinp

object Regegeg {

    fun sdf() {
        val esc = "\\s*\\\\.*".toRegex()
        val raw = """\s*\\.*""".toRegex()
        esc.matches("   \\comment") // true
        raw.matches("\\ another one") // true
    }
}