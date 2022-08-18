package com.mathroda.dashcoin.presentation.signin_screen.state

import com.google.firebase.auth.AuthResult

data class SignInState (
    val isLoading: Boolean = false,
    val signIn: AuthResult?= null,
    val error: String? = null
       )