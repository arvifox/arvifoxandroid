package com.arvifox.arvi.domain.multithre

private class Foo(var a: Int = 0, var b: Int = 0,)

object TesIntMul {

    fun check() {
        val f = Foo()
        val t1 = Thread {
            f.a = 1
            println("b=${f.b}")
        }
        val t2 = Thread {
            f.b = 1
            println("a=${f.a}")
        }
        t1.start()
        t2.start()
        t1.join()
        t2.join()
    }
}
