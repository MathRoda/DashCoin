package com.mathroda.datasource.providers

import androidx.compose.runtime.MutableState
import com.mathroda.core.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface ProvidersRepository {

    suspend fun userStateProvider (
        function: () -> Unit
    ): Flow<UserState>
}