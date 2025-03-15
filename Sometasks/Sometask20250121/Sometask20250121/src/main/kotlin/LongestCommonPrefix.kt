package com.arvifox

/*
https://leetcode.com/problems/longest-common-prefix/description/
 */

fun main() {
    val rere = Rere()
    val ss = rere.readRes("/longestcommon.txt")
    val res = doit(ss.toTypedArray())
    println("done=[$res]")
}

private fun doit(strs: Array<String>): String {

    fun letterequal(p: Int): Boolean {
        val c = strs.getOrNull(0)?.getOrNull(p)
        return strs.all { it.getOrNull(p) != null && it.getOrNull(p) == c }
    }

    var prefix = ""
    var ind = 0
    while (true) {
        if (letterequal(ind)) {
            prefix += strs[0][ind]
            ind++
        } else {
            break
        }
    }
    return prefix
}
