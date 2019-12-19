package com.arvifox.arvi.domain.misc

object Miscc02 {
    fun dfdf() {
        val w = Widd()
        val li = { wi: Widd -> println("wii = $wi") }
        w.addLis(li)
        println("count=${w.count}")
        w.removeLis(li)
        println("count=${w.count}")
    }

    fun dfdf22() {
        val w = Widd()
        val li = Widd.Listener { wi: Widd -> println("wii = $wi") }
        w.addLis(li)
        println("count=${w.count}")
        w.removeLis(li)
        println("count=${w.count}")
    }
}