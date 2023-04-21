package com.mathroda.datasource.usecases

import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import javax.inject.Inject

class CacheUserDataUseCase @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
) {

    suspend operator fun invoke() {
        dashCoinRepository.getDashCoinUser().collect{ user ->
            if (user != null) {
                return@collect
            }

            firebaseRepository.getUserCredentials().collect {
                when (it) {
                    is Resource.Success -> it.data?.run { dashCoinRepository.cacheDashCoinUser(this) }
                    else -> {}
                }
            }
        }
    }
}