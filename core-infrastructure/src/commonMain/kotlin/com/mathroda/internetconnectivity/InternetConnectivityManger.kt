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

class InternetConnectivityMangerImpl(
    private val konnectivity: Konnectivity
): InternetConnectivityManger {
    override val isConnected: Boolean
        get() = konnectivity.isConnected

    override fun getState(): Flow<InternetState> {
        return konnectivity.currentNetworkConnectionState
            .map { it.toInternetState() }
    }

    private fun NetworkConnection.toInternetState() =
        when(this) {
            NetworkConnection.NONE -> InternetState.NotAvailable
            NetworkConnection.CELLULAR -> InternetState.Available
            NetworkConnection.WIFI -> InternetState.Available
            else -> InternetState.IDLE
        }

}