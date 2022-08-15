package com.mathroda.dashcoin.domain.use_case.auth.signup

import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import javax.inject.Inject

class IsCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

    operator fun invoke() =
        firebaseRepository.isCurrentUserExist()
}