package com.mathroda.datasource.usecases

import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository

class SignOutUseCase (
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository,
    private val dataStoreRepository: DataStoreRepository
){
    suspend operator fun invoke() {
        dashCoinRepository.removeDashCoinUserRecord()
        dashCoinRepository.removeAllFavoriteCoins()
        firebaseRepository.signOut()
        dataStoreRepository.saveIsUserExist(false)
    }
}