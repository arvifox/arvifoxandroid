package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CoroChannel {
    val cha = Channel<Int>()
    suspend fun sendInt(a: Int) {
        cha.send(a)
    }

    suspend fun consumeInt() {
        for (a in cha) {
            println("channel int=$a")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun asd() = coroutineScope {
        val ccc: ReceiveChannel<Long> = produce {
            send(123L)
        }
    }

    suspend fun qwe() {
        flow {
            withContext(Dispatchers.IO) {
                emit(123)
            }
        }.collect {
            withContext(Dispatchers.Main) {

            }
        }

        channelFlow {
            launch {

            }
            withContext(Dispatchers.IO) {
                send(123)
            }
        }.collect {
            withContext(Dispatchers.Main) {

            }
        }
    }
}