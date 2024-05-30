package com.arvifox.arvi.domain.multithre

import java.util.concurrent.CountDownLatch

private class Foo(var a: Int = 0, var b: Int = 0)

object TesIntMul {

    /**
     * possible output: b=0,a=1 ; a=0,b=1 ; 0,0 seldom ; 1,1 seldom
     */
    fun check() {
        repeat(10000) {
            println()
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
        println("done d")
    }
}

// ===============

private class Foo2(
    @Volatile
    var a: Int = 0,
    @Volatile
    var b: Int = 0,
)

object TesIntMul2 {

    /**
     * possible output: b=0,a=1 ; a=0,b=1 ; 1,1
     */
    fun check2() {
        repeat(10000) {
            println()
            val f = Foo2()
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
        println("done d")
    }
}

// ===============

private class Foo3(
    var a: Int = 0,
    var b: Int = 0,
)

object TesIntMul3 {

    /**
     * possible output: 1,1
     */
    fun check3() {
        repeat(10000) {
            val la = CountDownLatch(2)
            println()
            val f = Foo3()
            val t1 = Thread {
                f.a = 1
                la.countDown()
                la.await()
                println("b=${f.b}")
            }
            val t2 = Thread {
                f.b = 1
                la.countDown()
                la.await()
                println("a=${f.a}")
            }
            t1.start()
            t2.start()
            t1.join()
            t2.join()
        }
        println("done d")
    }
}
