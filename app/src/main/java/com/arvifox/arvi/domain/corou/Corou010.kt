package com.arvifox.arvi.domain.corou

import com.arvifox.arvi.domain.corou.Arv10.bgDispatcher
import kotlinx.coroutines.*

// https://proandroiddev.com/kotlin-coroutines-patterns-anti-patterns-f9d12984c68e

object Arvi15 {

    // execute two tasks sequentially

    val uiScope = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope.launch {
        // show progress

        val result1 = withContext(bgDispatcher) {
            // background thread
            // your blocking call
        }

        val result2 = withContext(bgDispatcher) {
            // background thread
            // your blocking call
        }

        //val result = result1 + result2

        //hide progress
    }
}

object Arvi16 {

    // execute two tasks parallel

    val uiScope = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope.launch {
        // show progress

        val task1 = async(bgDispatcher) {
            // background thread
            // your blocking call
        }

        val task2 = async(bgDispatcher) {
            // background thread
            // your blocking call
        }

        //val result = task1.await() + task2.await()

        // hide progress
    }

}