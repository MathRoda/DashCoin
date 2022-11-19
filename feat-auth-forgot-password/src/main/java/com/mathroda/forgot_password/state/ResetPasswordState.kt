package com.mathroda.forgot_password.state


data class ResetPasswordState(
    val isLoading: Boolean = false,
    val Successful: Boolean = false,
    val error: String = ""
)