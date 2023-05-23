package jp.co.soramitsu.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

fun <T> Any.unsafeCast(): T {
    @Suppress("UNCHECKED_CAST")
    return this as T
}

inline fun <reified T> Any?.safeCast(): T? {
    return if (this != null && this is T) this else null
}

class NetworkStateListener {

    enum class State {
        CONNECTED, DISCONNECTED
    }

    private val stateSubject = MutableStateFlow<State?>(null)
    private var connectivityManager: ConnectivityManager? = null
    private val connections = mutableSetOf<String>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            stateSubject.value = State.CONNECTED
            connections.add(network.toString())
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            connections.remove(network.toString())
            if (connections.isEmpty()) {
                stateSubject.value = State.DISCONNECTED
            }
        }
    }

    fun subscribe(context: Context): Flow<State> {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE).safeCast<ConnectivityManager>()
                ?.apply {
                    val networkRequestBuilder = NetworkRequest.Builder().build()
                    registerNetworkCallback(networkRequestBuilder, networkCallback)
                }
        return stateSubject.asStateFlow().filterNotNull().distinctUntilChanged()
    }

    fun release() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        connectivityManager = null
    }
}
