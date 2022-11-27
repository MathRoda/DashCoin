package com.mathroda.datasource.providers

import androidx.compose.runtime.MutableState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.firebase.FirebaseRepository
import javax.inject.Inject

class ProvidersRepositoryImpl @Inject constructor(
    private val firebase: FirebaseRepository
): ProvidersRepository{
    override suspend fun uiStateProvider(
        state: MutableState<UserState>,
        function: () -> Unit
    ) {
        firebase.isCurrentUserExist().collect {
            when (it) {
                false -> state.value = UserState.UnauthedUser

                true -> {
                    firebase.getUserCredentials().collect { result ->
                        when(result) {
                            is Resource.Success -> {
                                result.data?.let { user ->
                                    if (user.isUserPremium()) {
                                        state.value = UserState.PremiumUser
                                    } else {
                                        state.value = UserState.AuthedUser
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                   function()
                }
            }
        }
    }

}