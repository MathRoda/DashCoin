package com.mathroda.datasource.providers

import androidx.compose.runtime.MutableState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.firebase.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProvidersRepositoryImpl @Inject constructor(
    private val firebase: FirebaseRepository
): ProvidersRepository{
    override suspend fun userStateProvider(
        function: () -> Unit
    ): Flow<UserState> {
        return flow {
            firebase.isCurrentUserExist().collect {
                when (it) {
                    false ->  emit(UserState.UnauthedUser)

                    true -> {
                        firebase.getUserCredentials().collect { result ->
                            when(result) {
                                is Resource.Success -> {
                                    result.data?.let { user ->
                                        if (user.isUserPremium()) {
                                           emit(UserState.PremiumUser)
                                        } else {
                                           emit(UserState.AuthedUser)
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

}