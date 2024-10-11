package com.arvifox.componavi

import androidx.annotation.AnyThread
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.Serializable

class NavManager {

    private val _navFlow = Channel<NavEvent>()
    val navFlow = _navFlow.receiveAsFlow()

    suspend fun pushEvent(d: NavEvent) {
        _navFlow.send(d)
    }
}

sealed interface NavEvent {
    data object Pop : NavEvent
    data class PopWithResult(val result: NavEventResult) : NavEvent
    data class NavTo(val d: Dest) : NavEvent
}

data class NavEventResult(val k: String, val result: String)

sealed interface Dest {
    @Serializable
    data object Splash : Dest

    @Serializable
    data object Login : Dest

    @Serializable
    data class Detail(val arg: String) : Dest
}