package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object Arv10 {

    private suspend fun getFoo(s: Int): String {
        return s.toString()
    }

    private suspend fun getUp(): String {
        val dd = getFoo(12)
        println("before 3000")
        delay(3000)
        println("after 3000")
        return "get-up $dd"
    }

    fun getPP() {
        val b = runBlocking {
            val bb = async { getUp() }.await()
            println("after await")
            return@runBlocking "sdlsf$bb"
        }
        println("=$b")
        val job11 = 7
    }
}

interface IApi {
    suspend fun getFoo(f: Int): String
}