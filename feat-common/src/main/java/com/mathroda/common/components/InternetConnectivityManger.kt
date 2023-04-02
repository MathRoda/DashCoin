package com.mathroda.common.components

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@SuppressLint("MissingPermission")
@Composable
fun InternetConnectivityManger(
    onInternetAvailable: () -> Unit
) {
    val context = LocalContext.current
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    val networkRequest = remember {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    DisposableEffect(connectivityManager, networkRequest) {
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onInternetAvailable()
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callBack)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callBack)
        }
    }
}