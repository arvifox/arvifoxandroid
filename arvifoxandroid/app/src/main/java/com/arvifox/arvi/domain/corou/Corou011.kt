package com.arvifox.arvi.domain.corou

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.arvifox.arvi.domain.corou.Arv10.bgDispatcher
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Arvi17 {

    val uiScope = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope.launch {
        val task = async(bgDispatcher) {
            // background thread
            // your blocking call
        }
        // suspend until task is finished or return null in 2 sec
        val result = withTimeoutOrNull(2000) { task.await() }
    }
}

object Arvi_supervisor {

    var job = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    fun startPresenting() {
        loadData()
    }
    fun stopPresenting() {
        uiScope.coroutineContext.cancelChildren()
    }
    private fun loadData() = uiScope.launch {
        //show pr
        val result = withContext(bgDispatcher) { // background thread
            // your blocking call
        }
        //hide pr
    }
}

object Arvi_lifecycler {

    class MainScope : CoroutineScope, LifecycleObserver {

        private val job = SupervisorJob()
        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.Main

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun destroy() = coroutineContext.cancelChildren()
    }
    // usage
    class MainFragment : Fragment() {
        private val uiScope = MainScope()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            lifecycle.addObserver(uiScope)
        }

        private fun loadData() = uiScope.launch {
            val result = withContext(bgDispatcher) {
                // your blocking call
            }
        }
    }
}