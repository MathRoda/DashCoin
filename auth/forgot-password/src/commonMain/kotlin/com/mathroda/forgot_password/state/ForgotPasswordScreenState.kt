package com.mathroda.forgot_password.state

data class ForgotPasswordScreenState(
    val email: String = "",
    val isError: Boolean = false,
    val isVisible: Boolean = true,
    val isLoading: Boolean = false,
)
