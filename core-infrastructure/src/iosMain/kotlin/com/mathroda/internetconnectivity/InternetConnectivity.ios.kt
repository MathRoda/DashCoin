package com.mathroda.internetconnectivity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InternetConnectivityImpl: InternetConnectivityManger {
    override val isConnected: Boolean
        get() = true

    override fun getState(): Flow<InternetState> {
        return flow {
            emit(InternetState.Available)
        }
    }

}