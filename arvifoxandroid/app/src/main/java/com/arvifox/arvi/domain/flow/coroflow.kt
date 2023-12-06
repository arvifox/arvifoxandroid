package com.arvifox.arvi.domain.flow

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

object dfff {

    @Parcelize
    class Gfdf(val df: Int) : Parcelable

    interface Fff {
        fun getFlowData(g: Int): Flow<Gfdf>
    }

    val oneElementFlow: Flow<Int> = flow {
        // producer block starts here, stream starts
        emit(1)
        // producer block finishes here, stream will be closed
    }

    val unlimitedElementFlow: Flow<Int> = flow {
        // producer block starts here, stream starts
        while (true) {
            // Do calculations
            emit(123)
            delay(100)
        }
        // producer block finishes here, stream will be closed
    }

}

object ccorfgf {
    fun obser(): Flow<Int> {
        return flow {
            emit(123)
        }
    }

    val d = CoroutineScope(Job() + Dispatchers.Main).launch {
        obser()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                println("$result")
            }
            .launchIn(this)

        obser()
            .flowOn(Dispatchers.IO)
            .onEach { result ->
                println("$result second")
            }
            .launchIn(this)
    }
}

object iuuwe {
    val dd = Executors.newFixedThreadPool(3).asCoroutineDispatcher()

    val d = CoroutineScope(Job() + Dispatchers.Main).launch {
        flowOf(1, 2, 3)
            .onEach { printThread("1") }
            .flowOn(Dispatchers.IO)
            .onEach { printThread("2") }
            .flowOn(Dispatchers.Default)
            .flatMapMerge {
                flowOf(4, 5, 6)
                    .onEach { printThread("inner 1") }
            }
            .onEach { printThread("3") }
            .collect { printThread("end") }
    }

    fun printThread(s: String) {
        println("thread $s, ${Thread.currentThread().name}")
    }
}