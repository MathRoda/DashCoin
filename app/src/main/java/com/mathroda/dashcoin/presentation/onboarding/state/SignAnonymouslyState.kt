package com.mathroda.dashcoin.presentation.onboarding.state

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

data class SignAnonymouslyState(
    val isLoading: Boolean = false,
    val success: FirebaseUser? = null,
    val error: String = ""
)
