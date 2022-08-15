package com.mathroda.dashcoin.domain.use_case.auth.signup

import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

    operator fun invoke(email: String, password: String) =
        firebaseRepository.signUpWithEmailAndPassword(email, password)
}