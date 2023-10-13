package com.mathroda.internetconnectivity

import kotlinx.coroutines.flow.Flow

sealed class InternetState {
    object Available: InternetState()
    object NotAvailable: InternetState()
    object IDLE: InternetState()
}
interface InternetConnectivityManger {
    fun getState(): Flow<InternetState>
}