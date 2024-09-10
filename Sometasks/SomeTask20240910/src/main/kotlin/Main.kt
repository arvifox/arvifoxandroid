package com.arvifox

import kotlin.math.max

fun main() {
//    val inputString = "97++085*84+742+0928+0++675"
    val inputString = "9+6++2*0*+123"
    val result = solution(inputString)
    println("answer = $result")
}

private fun solution(from: String): Int {
    val s = "+$from+"
    val v = mutableListOf<Int>()
    s.forEachIndexed { index, c ->
        if (c == '+' || c == '*') v.add(index)
    }
    val n = mutableListOf<String>()
    var t: String = ""
    for (i in 0..<(v.size - 1)) {
        val len: Int = v[i + 1] - v[i] - 1
        var p: String = s.substring(v[i] + 1, v[i] + 1 + len)
        var f: Boolean = false
        if (p.length > 1) {
            if (p[0] == '0') {
                f = true
            }
        }
        if (p.isEmpty() || f) {
            if (t.isNotEmpty()) {
                n.add(t.substring(0, t.length - 1))
                if (f) {
                    n[n.size - 1] += s.substring(v[i], v[i] + 2)
                }
            }
            while (p.isNotEmpty() && p[0] == '0') {
                p = p.removePrefix("0")
            }
            t = p + s[v[i + 1]]
            if (t.length == 1) {
                t = ""
            }
        } else {
            t += (p + s[v[i + 1]])
        }
    }
    n.add(t)
    var lenmax = -1
    for (i in 0..n.lastIndex) {
        var m = mutableListOf<Int>()
        var r: String = "+${n[i]}+"
        for (j in 0..r.lastIndex) {
            if (r[j] == '+') {
                m.add(j)
            }
        }
        var lencur = 0
        for (j in 0..<(m.size - 1)) {
            val len = m[j + 1] - m[j] - 1
            val p: String = r.substring(m[j] + 1, m[j] + 1 + len)
            if (p.indexOf('0') != -1 && (p.length == 1 || p.indexOf('*') != -1)) {
                lencur += (len + 1)
            } else {
                lencur = 0
                if (p.isNotEmpty() && p[p.lastIndex] == '0') {
                    lencur = 2
                }
            }
            lenmax = max(lenmax, lencur - 1)
        }
    }
    return lenmax
}
