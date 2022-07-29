package com.arvifox.arvi.domain.corou

import android.widget.TextView
import kotlinx.coroutines.*

object Corou021 {
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    private suspend inline fun <T> await(
        coroutineScope: CoroutineScope,
        crossinline block: suspend Job.() -> T
    ): T {
        val deferred = CompletableDeferred<T>()
        coroutineScope.launch {
            try {
                val result = deferred.block()
                deferred.complete(result)
            } catch (throwable: Throwable) {
                deferred.completeExceptionally(throwable)
            }
        }

        return deferred.await()
    }

    private suspend inline fun <T> doAsync(crossinline block: suspend () -> T): T {
        return await(ioScope) { block() }
    }

    val job = SupervisorJob()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private fun launchMyJob(block: suspend () -> Unit) {
        mainScope.launch(job) {
            try {
                block()
            } catch (e: CancellationException) {
            }
        }
    }

    fun performLongRunningTasks(param1: Int, param2: Int, textView: TextView) {
        launchMyJob {
            val result1 = doAsync { someBigTask(param1) }
            val result2 = doAsync { someBigTask(result1, param2) }
            textView.text = result2.toString()
        }
    }

    fun someBigTask(i: Int): Int = i

    fun someBigTask(i: Int, j: Int): Int = i + j
}