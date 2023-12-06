package com.arvifox.arvi.domain.sam

/**
 * [https://jorgecastillo.dev/kotlin-sam-conversions]
 * Single Abstract Method
 */
object KotlinSam {

    val logger = object : Consumerka<String> {
        override fun consume(a: String) {
            println(a)
        }
    }

    val logger2: Consumerka<String> = Consumerka<String> { a -> println(a) }

    fun asdf() {
        consu { d: String -> println(d) }
        consu(object : Consumerka<String> {
            override fun consume(a: String) {

            }
        })
    }

    fun consu(l: Consumerka<String>) = l.consume("asd")
}

fun interface Consumerka<A> {
    fun consume(a: A)
}

//interface Consumerka<A> {
//    fun consume(a: A)
//}