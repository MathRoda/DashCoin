package com.mathroda.dashcoin.domain.use_case.auth.get_user

import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
){

    operator fun invoke() =
        firebaseRepository.getCurrentUser()
}