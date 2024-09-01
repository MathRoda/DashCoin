package com.mathroda.signup.state

import com.mathroda.domain.DashCoinUser

data class SignUpState(
    val isLoading: Boolean = false,
    val signUp: DashCoinUser? = null,
    val error: String = ""
)
