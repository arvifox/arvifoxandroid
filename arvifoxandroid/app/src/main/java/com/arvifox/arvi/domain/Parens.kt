package com.arvifox.arvi.domain

object QuestionB {
    private fun addParen(
        list: ArrayList<String>,
        leftRem: Int,
        rightRem: Int,
        str: CharArray,
        index: Int
    ) {
        if (leftRem < 0 || rightRem < leftRem) return
        if (leftRem == 0 && rightRem == 0) {
            list.add(String(str))
        } else {
            str[index] = '('
            addParen(list, leftRem - 1, rightRem, str, index + 1)
            str[index] = ')'
            addParen(list, leftRem, rightRem - 1, str, index + 1)
        }
    }

    private fun generateParens(count: Int): ArrayList<String> {
        val str = CharArray(count * 2)
        val list: ArrayList<String> = ArrayList<String>()
        addParen(list, count, count, str, 0)
        return list
    }

    @JvmStatic
    fun main() {
        val list = generateParens(6)
        for (s in list) {
            println(s)
        }
        println(list.size)
    }
}