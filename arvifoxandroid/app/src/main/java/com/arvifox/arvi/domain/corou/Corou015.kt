package com.arvifox.arvi.domain.corou

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Corou015 {
    fun sdf(ed: EditText) {
        ed.doAfterTextChanged {
            CoroutineScope(GlobalScope.coroutineContext).launch {
//                queryChannel.send(it.toString())
            }
        }
    }

//    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    fun geee() {
//        val rr = queryChannel.asFlow().debounce(500).mapLatest {
//            try {
//                startRequest(it)
//            } catch (e: CancellationException) {
//
//            }
//        }.catch {  }
    }

    private fun startRequest(s: String) {
    }
}
