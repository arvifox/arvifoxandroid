package com.arvifox.arvi.domain.kotlinp

// https://www.youtube.com/watch?v=t387acWEK3o

object Kotlipu {

    private fun hello(): Boolean {
        println(print("Hello") == print("World") == return false)
    }

    fun statr() {
        val r = hello()
        println(r)
    }

    private fun printint(n: Int) {
        println(n)
    }

    fun start02() {
        //printint(-2_147_483_648.inc())
    }

    fun start03() {
        var x: UInt = 0u
        println(x--.toInt())
        println(--x)
    }

    fun start04() {
        var cells = arrayOf(arrayOf(1, 1, 1), arrayOf(0, 1, 1), arrayOf(1, 0, 1))
        var ne =
            cells[0][0] + cells[0][1] + cells[0][2]
        +cells[1][0] + cells[1][2]
        +cells[2][0] + cells[2][1] + cells[2][2]
        print(ne)
    }

    fun start05() {
        val x: Int? = 2
        val y: Int = 3
        val sum = x ?: 0 + y
        println(sum)
    }

    @DslMarker annotation class BeerLang

    @BeerLang data class Recipe(var name: String? = null, var hops: List<Hops> = emptyList())
    @BeerLang data class Hops(var kind: String? = null, var atMinute: Int = 0, var grams: Int = 0)
    private fun beer(build: Recipe.() -> Unit) = Recipe().apply(build)
    private fun Recipe.hops(build: Hops.() -> Unit) {hops += Hops().apply(build)}
    fun start06() {
        val recipe = beer {
            name = "Si"
            hops {
//                name = "Ca"
                kind = "Ca"
                grams = 100
                atMinute = 15
            }
        }
        println(recipe)
    }

    fun start07() {
        fun f(x: Boolean) {
            when (x) {
                (x) -> println("$x TRUE")
                else -> println("$x FALSE")
            }
        }
        f(true)
        f(false)
    }

    abstract class NullSafeLang {
        abstract val name: String
        val logo = name[0].toUpperCase()
    }
    class Kotlin : NullSafeLang() {
        override val name = "kotlin"
//        override val name get() = "kotlin"
    }
    fun start08() {
        println(Kotlin().logo)
    }


    fun start09() {
        val result = mutableListOf<() -> Unit>()
        var i = 0
        for (j in 1..3) {
            i++
            result += { print("$i $j ; ")}
        }
        result.forEach { it() }
    }


    fun start10() {
        fun foo(a: Boolean, b: Boolean) = print("$a, $b")
        val aa = 1
        val bb = 2
        val cc = 3
        val dd = 4
//        foo(cc < aa, bb > dd)
        foo(cc > aa, bb > dd)
    }


    data class Container(val name: String, private val items: List<Int>) : List<Int> by items
    fun start11() {
        val (name, items) = Container("Kotlin", listOf(1,2,3))
        println("Hello $name, $items")
    }


    fun start12() {
        fun <T> Any?.asGeneric() = this as? T
        42.asGeneric<Nothing>()!!!!
        val a = if (true) 87
        println(a)
    }


//    open class A(val x: Any) {
    open class A(val x: Any?) {
        override fun toString() = javaClass.simpleName
    }
    object B : A(C)
    object C : A(B)
    fun start14() {
        println(B.x)
        println(C.x)
    }


    fun start15() {
        val x = sequence {
            var n = 0
            while (true) yield(n++)
        }
        println(x.take(3))
//        println(x.take(3).toList())
    }


    fun start16() {
        val what = {->}.fun
        Function<*>.(){}()
        println(what)
    }
}