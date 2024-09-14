package com.mathroda.internetconnectivity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
class InternetConnectivityManagerImpl(
    application: Application
): InternetConnectivityManger {
    private val connectivityManger = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    override val isConnected: Boolean
        get() = connectivityManger.isConnected()

    override fun getState(): Flow<InternetState> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(InternetState.Available)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(InternetState.NotAvailable)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(InternetState.NotAvailable)
                }
            }

            connectivityManger.registerNetworkCallback(networkRequest, callback)

            awaitClose { connectivityManger.unregisterNetworkCallback(callback) }
        }
    }

    private fun ConnectivityManager.isConnected(): Boolean {
        val network = activeNetwork ?: return false
        val activeNetwork = getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}