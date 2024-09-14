package com.mathroda.internetconnectivity

import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class InternetState {
    data object Available: InternetState()
    data object NotAvailable: InternetState()
    data object IDLE: InternetState()
}

interface InternetConnectivityManger {
    val isConnected: Boolean

    fun getState(): Flow<InternetState>
}