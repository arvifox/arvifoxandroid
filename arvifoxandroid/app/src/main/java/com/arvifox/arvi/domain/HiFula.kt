package com.arvifox.arvi.domain

// https://blog.kotlin-academy.com/mastering-kotlin-scoped-and-higher-order-functions-23e2dd34d660

object Arvi00 {
    private fun sdf(er: Int, df: Doo.() -> String, kkj: Double): String {
        val dd = er.toString()
        val fs = Doo()
        return dd.plus(fs.df())
    }

    fun sdff() {
        val sdf = sdf(23, { this.getD() + "999" }, 44.4)
        println(sdf)
    }

    private class Doo {
        fun getD(): String {
            return "we3"
        }
    }

    private fun doSome(block: () -> Unit) {
        block()
    }

    private fun someS() {
        println()
    }

    fun sooo() {
        doSome(::someS)
    }

    fun kjlskfd() {
        doSome(fun() { println() })
    }

    interface Isdlfkj : () -> Unit {

    }

    class Fkjdf : Isdlfkj {
        override fun invoke() {
            sum(12, 34)
        }
    }

    val sum: (Int, Int) -> Int = {x: Int, y: Int -> x + y}

    fun<T,R> T.doSomething(block: T.() -> R) {
        println(block())
    }

    fun <T, R> T.mylet(block: (T) -> R): R {
        return block(this)
    }

    fun <T> T.myapply(block: T.() -> Unit): T {
        block()
        return this
    }

    fun <T> T.myalso(block: (T) -> Unit): T {
        block(this)
        return this
    }

    fun <T, R> T.myrun(block: T.() -> R): R {
        return block()
    }

    fun <T, R> mywith(receiver: T, block: T.() -> R): R {
        return receiver.block()
    }
}