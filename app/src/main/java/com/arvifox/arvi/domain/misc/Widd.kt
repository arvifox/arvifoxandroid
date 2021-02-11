package com.arvifox.arvi.domain.misc

class Widd {
    fun interface Listener {
        fun onEvent(w: Widd)
    }

    private val count = mutableListOf<Listener>()

    fun getCount(): Int = count.size

    fun addLis(l: Listener) {
        count.add(l)
    }

    fun removeLis(l: Listener) {
        count.remove(l)
    }
}