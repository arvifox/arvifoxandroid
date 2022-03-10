package com.arvifox.arvi.domain.corou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

object Arvi013_cd {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun cancel() {
        viewModelJob.cancel()
//        uiScope.coroutineContext.cancel()
    }

    fun sdf() {

    }
}

class MyViewModel : ViewModel() {

    fun longtask() {
        viewModelScope.launch(Dispatchers.IO) {
            // some task

            val a = withTimeout(123) {

            }
        }
    }

    fun jk() {
        viewModelScope.launch {
            delay(1_000)
            //
        }
    }

    // No need to override onCleared()
}