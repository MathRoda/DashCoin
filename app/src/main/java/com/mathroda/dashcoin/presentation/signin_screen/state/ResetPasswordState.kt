package com.mathroda.dashcoin.presentation.signin_screen.state

import com.google.firebase.auth.AuthResult

data class ResetPasswordState (
    val isLoading: Boolean = false,
    val signIn: Void?= null,
    val error: String = ""
       )