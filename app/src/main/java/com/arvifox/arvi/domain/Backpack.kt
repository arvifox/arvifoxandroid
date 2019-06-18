package com.arvifox.arvi.domain

object Backpack {
    private fun calcBackpack(sum: Int, weights: IntArray): Int {
        val d = IntArray(sum + 1)
        d[0] = 1
        for (w in weights) {
            for (i in sum downTo w) {
                if (d[i - w] == 1) {
                    d[i] = 1
                }
            }
        }
        return d.asSequence().indexOfLast { x -> x > 0 }
    }

    fun startBackPack() {
        calcBackpack(20, intArrayOf(12, 23, 45))
    }
}