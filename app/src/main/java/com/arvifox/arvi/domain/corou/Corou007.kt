package com.arvifox.arvi.domain.corou

object Arv10 {

    private suspend fun getFoo(s: Int): String {
        return s.toString()
    }

    suspend fun getUp() {
        val dd = getFoo(12)
    }

    fun getPP() {
        val job11 = 7
    }
}