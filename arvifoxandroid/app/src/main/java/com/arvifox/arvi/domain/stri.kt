package com.arvifox.arvi.domain

object stri {
    data class PathParts(val dir: String, val name: String)

    val pattern = Regex("(.+)/([^/]*)")
    fun split(path: String): PathParts {
        val m = pattern.matchEntire(path) ?: return PathParts("", path)
        return PathParts(m.groupValues[1], m.groupValues[2])
    }

    fun split2(path: String) =
        PathParts(path.substringBeforeLast('/', ""), path.substringAfterLast('/'))

    val lang = "Kotlin"
    fun df() {
        lang should startWith("Kot")
    }

    infix fun <T> T.should(verifier: (T) -> Unit) = verifier(this)
    private fun startWith(prefix: String) = { value: String ->
        if (!value.startsWith(prefix)) throw AssertionError("")
    }
}