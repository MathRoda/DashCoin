package com.mathroda.datasource.usecases

import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CacheUserDataUseCase @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
) {

    suspend operator fun invoke() {
       val user =  dashCoinRepository.getDashCoinUser()
        if (user != null) {
            return
        }

        firebaseRepository.getUserCredentials().collectLatest { result ->
            if (result is Resource.Success) {
                result.data?.run { dashCoinRepository.cacheDashCoinUser(this) }
            }
        }
    }
}