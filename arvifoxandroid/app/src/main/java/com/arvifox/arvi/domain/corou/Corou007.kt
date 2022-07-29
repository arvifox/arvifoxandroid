package com.arvifox.arvi.domain.corou

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import java.util.function.BiFunction

object Arv10 {

    // dispatches execution into Android main thread
    val uiDispatcher: CoroutineDispatcher = Dispatchers.Main

    // represent a pool of shared threads as coroutine dispatcher
    val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

    private suspend fun getFoo(s: Int): String {
        return s.toString()
    }

    private suspend fun getUp(): String {
        val dd = getFoo(12)
        println("before 3000 ${Thread.currentThread().name}")
        delay(3000)
        println("after 3000")
        return "get-up $dd"
    }

    fun getPP() {
        val b = runBlocking {
//            val bb = async { getUp() }.await()
            val bb = launch { getUp() }
            println("after coro ${Thread.currentThread().name}")
            return@runBlocking "sdlsf"
        }
        println("${Thread.currentThread().name} =$b")
        val job11 = 7
    }

    fun getWW() {
        println("${Thread.currentThread().name} =start")
        getPP()
        println("${Thread.currentThread().name} =stop")
    }

    fun glo() {
        val gl = GlobalScope.launch {  }
    }

}

interface IApi {
    suspend fun getFoo(f: Int): String
}

class AndroidJob(lifecycle: Lifecycle) : Job by Job(), LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.d("AndroidJob", "Cancelling a coroutine")
        cancel()
    }
}

object Arvi12 {

    suspend fun <T> retryDeferredWithDelay(
        deferred: () -> Deferred<T>,
        tries: Int = 3,
        timeDelay: Long = 1000L
    ): T {

        for (i in 1..tries) {
            try {
                return deferred().await()
            } catch (e: Exception) {
                if (i < tries) delay(timeDelay) else throw e
            }
        }
        throw UnsupportedOperationException()
    }

    @SuppressLint("NewApi")
    suspend fun <T1, T2, R> zip(
        source1: Deferred<T1>,
        source2: Deferred<T2>,
        zipper: BiFunction<T1, T2, R>
    ): R {
        return zipper.apply(source1.await(), source2.await())
    }

    @SuppressLint("NewApi")
    suspend fun <T1, T2, R> Deferred<T1>.zipWith(
        other: Deferred<T2>,
        zipper: BiFunction<T1, T2, R>
    ): R {
        return zip(this, other, zipper)
    }
}