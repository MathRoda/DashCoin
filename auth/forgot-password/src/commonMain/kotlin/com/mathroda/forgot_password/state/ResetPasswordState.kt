package com.mathroda.forgot_password.state


data class ResetPasswordState(
    val isLoading: Boolean = false,
    val successful: Boolean = false,
    val error: String = ""
)