package com.arvifox.arvi.tasks

import org.junit.Test

class RecTask {

    @Test
    fun str() {
        fun rec(s: String) {
            if (s.length == 1) {
                print(s)
            } else {
                print(s[s.lastIndex])
                rec(s.substring(0, s.lastIndex))
            }
        }
        val str = "hello world"
        rec(str)
    }

    @Test
    fun tripa() {
        fun printValue(row: Int, col: Int): Int {
            if (col > row) {
                return -1
            } else if (col == row || col == 1) {
                return 1
            } else {
                return printValue(row - 1, col) + printValue(row - 1, col - 1)
            }
        }
        println(printValue(6, 4))
    }
}