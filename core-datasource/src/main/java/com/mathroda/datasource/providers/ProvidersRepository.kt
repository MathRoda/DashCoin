package com.mathroda.datasource.providers

import androidx.compose.runtime.MutableState
import com.mathroda.core.state.UserState
import kotlinx.coroutines.flow.MutableStateFlow

interface ProvidersRepository {

    suspend fun uiStateProvider (
        state: MutableState<UserState>,
        function: () -> Unit
    )
}