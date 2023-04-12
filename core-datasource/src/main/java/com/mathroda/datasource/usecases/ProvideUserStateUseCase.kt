package com.mathroda.datasource.usecases

import com.mathroda.core.state.UserState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProvideUserStateUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository
) {

    operator fun invoke(function: () -> Unit): Flow<UserState> {
        return flow {
            if (!isUserExist()) {
                emit(UserState.UnauthedUser)
                return@flow
            }

            dashCoinRepository.isUserPremiumLocal().collect { isPremium ->
                if (!isPremium) {
                    emit(UserState.AuthedUser)
                }

                if (isPremium) {
                    emit(UserState.PremiumUser)
                }
            }

            function()
        }
    }

    private suspend fun isUserExist(): Boolean {
        var doesExist = false

        dataStoreRepository.readIsUserExistState.firstOrNull()?.let {
            if (it) {
                doesExist = true
            }

            if (!it) {
                doesExist = firebaseRepository.isUserExist()
                dataStoreRepository.saveIsUserExist(doesExist)
            }
        }

        return doesExist
    }
}