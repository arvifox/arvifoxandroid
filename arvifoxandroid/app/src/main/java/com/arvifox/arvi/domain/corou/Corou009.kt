package com.arvifox.arvi.domain.corou

import com.arvifox.arvi.domain.corou.Arv10.bgDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Arvi14 {
    val uiScope = CoroutineScope(Dispatchers.Main)

    fun loadData() = uiScope.launch {
        // show progress here

        val result = withContext(bgDispatcher) {
            // background thread
            // your blocking call
        }

        // hide progress here
    }
}