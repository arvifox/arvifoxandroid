package com.arvifox.arvi.domain.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface Connectivity {
    val connected: Flow<Boolean>
}

class AndroidConnectivity(
    context: Context,
) : Connectivity {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    override val connected: Flow<Boolean>
        get() = callbackFlow {
            val cb = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(false)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected =
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    trySend(connected)
                }
            }
            connectivityManager.registerDefaultNetworkCallback(cb)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(cb)
            }
        }
}
