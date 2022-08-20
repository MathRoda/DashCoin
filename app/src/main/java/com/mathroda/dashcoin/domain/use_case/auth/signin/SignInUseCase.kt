package com.mathroda.dashcoin.domain.use_case.auth.signin

import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(email: String, password: String) =
        firebaseRepository.signInWithEmailAndPassword(email, password)
}