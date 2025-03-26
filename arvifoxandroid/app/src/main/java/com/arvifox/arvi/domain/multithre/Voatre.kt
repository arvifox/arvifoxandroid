package com.arvifox.arvi.domain.multithre

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private val thrs = 100
private val opes = 1000

private class Vovola {
    @Volatile
    private var vo = 0

    fun letsdo() {
        val exec = Executors.newFixedThreadPool(thrs)
        val cdl = CountDownLatch(thrs)
        repeat(thrs) {
            exec.submit {
                repeat(opes) {
                    vo++
                }
                cdl.countDown()
            }
        }
        cdl.await()
        exec.shutdown()
        println("vo = $vo")
    }
}

fun main() {
    repeat(9) { a ->
        println("a=$a")
        val vovola = Vovola()
        vovola.letsdo()

        val ata = Voata()
        ata.letsdo()
    }
}

private class Voata {
    private var ata = AtomicInteger(0)

    fun letsdo() {
        val exec = Executors.newFixedThreadPool(thrs)
        val cdl = CountDownLatch(thrs)
        repeat(thrs) {
            exec.submit {
                repeat(opes) {
                    var cur = ata.get()
                    var new = cur + 1
                    while (!ata.compareAndSet(cur, new)) {
                        cur = ata.get()
                        new = cur + 1
                    }
                }
                cdl.countDown()
            }
        }
        cdl.await()
        exec.shutdown()
        println("ata = ${ata.get()}")
    }
}