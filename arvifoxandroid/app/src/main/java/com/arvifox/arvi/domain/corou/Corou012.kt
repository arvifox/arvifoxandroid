package com.arvifox.arvi.domain.corou

import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.domain.corou.Arv10.bgDispatcher
import com.arvifox.arvi.domain.corou.Arv10.uiDispatcher
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Arvi_exce {

    fun kjdflks() {
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
    }

    private fun loadData() = GlobalScope.async(uiDispatcher) {
        val result = withContext(bgDispatcher) {

        }
    }

    var j: Job? = null

    @OptIn(InternalCoroutinesApi::class)
    fun start() {
        j = loadData()
        j?.invokeOnCompletion {
            it?.printStackTrace()
        }
        j?.getCancellationException()?.printStackTrace()
    }

    val exce: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        // show throwable mesage
        j = Job()
    }

    private fun loadd() = GlobalScope.async(uiDispatcher + exce) {

    }
}