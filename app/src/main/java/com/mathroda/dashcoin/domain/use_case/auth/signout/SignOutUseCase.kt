package com.mathroda.dashcoin.domain.use_case.auth.signout

import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke() =
        firebaseRepository.signOut()
}