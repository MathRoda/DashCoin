package com.mathroda.internetconnectivity

import com.mathroda.core.state.InternetState
import kotlinx.coroutines.flow.Flow

interface InternetConnectivityManger {
    fun getState(): Flow<InternetState>
}