package com.mathroda.dashcoin.presentation.onboarding.state

import com.google.firebase.auth.AuthResult

data class SignAnonymouslyState(
    val isLoading: Boolean = false,
    val success: AuthResult? = null,
    val error: String = ""
)
