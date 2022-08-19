package com.mathroda.dashcoin.presentation.signup_screen.state

import com.google.firebase.auth.AuthResult

data class SignUpState(
    val isLoading: Boolean = false,
    val signUp: AuthResult?= null,
    val error: String = ""
)
