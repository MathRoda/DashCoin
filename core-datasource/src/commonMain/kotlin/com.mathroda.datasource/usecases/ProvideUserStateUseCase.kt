package com.mathroda.datasource.usecases

import com.mathroda.core.state.UserState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import kotlinx.coroutines.flow.firstOrNull

class ProvideUserStateUseCase (
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository
) {

    suspend operator fun invoke(): UserState {
        if (!isUserExist()) {
            return UserState.UnauthedUser
        }

        val isPremium =  dashCoinRepository.isUserPremiumLocal()
        if (!isPremium) {
            return UserState.AuthedUser
        }

        return UserState.PremiumUser
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