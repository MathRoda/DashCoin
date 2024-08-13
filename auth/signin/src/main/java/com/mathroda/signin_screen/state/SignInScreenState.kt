package com.mathroda.signin_screen.state

data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isError: Boolean = false,
    val isVisible: Boolean = true,
    val isLoading: Boolean = false,
)
