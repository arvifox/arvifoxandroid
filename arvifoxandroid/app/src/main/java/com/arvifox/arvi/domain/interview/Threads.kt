package com.arvifox.arvi.domain.interview

class InfoS {
    var a: Int = 0
        @Synchronized
        set
        @Synchronized
        get

    var b: Int = 0
}

class Info(
    var a: Int = 0,
    var b: Int = 0,
)

fun main() {
    val info = Info()
    val t1 =
        Thread {
            info.a = 1
            println("b=${info.b}")
        }
    val t2 =
        Thread {
            info.b = 1
            println("a=${info.a}")
        }

    t1.start()
    t2.start()

    t1.join()
    t2.join()

    println("done")
}
