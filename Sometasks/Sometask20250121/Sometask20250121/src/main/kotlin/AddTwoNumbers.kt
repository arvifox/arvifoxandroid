package com.arvifox

import kotlin.math.pow

private class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

fun main() {
    val l1 = ListNode(2).let { u1 ->
        u1.next = ListNode(4).let { u2 ->
            u2.next = ListNode(3)
            u2
        }
        u1
    }
    val l2 = ListNode(5).let { u1 ->
        u1.next = ListNode(6).let { u2 ->
            u2.next = ListNode(4)
            u2
        }
        u1
    }
    val res = add(l1, l2)
    var cu = res
    while (cu != null) {
        println("item=[${cu.`val`}]")
        cu = cu.next
    }
}

private fun sar(a: Array<Int>): Int {
    return a.sumOf {
        it
    }
}

private fun add(l1: ListNode?, l2: ListNode?): ListNode? {

    fun Int.intpow(p: Int): Int {
        return this.toDouble().pow(p).toInt()
    }

    var d1 = 0
    var d2 = 0
    var po = 0
    var lc1 = l1
    var lc2 = l2
    while (true) {
        if (lc1 == null && lc2 == null) { break }
        if (lc1 != null) {
            d1 += lc1.`val` * (10.intpow(po))
        }
        if (lc2 != null) {
            d2 += lc2.`val` * (10.intpow(po))
        }
        po++
        lc1 = lc1?.next
        lc2 = lc2?.next
    }
    val sum = d1 + d2
    return ListNode(sum)
}