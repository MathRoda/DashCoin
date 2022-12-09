package com.mathroda.profile_screen.drawer.state

data class UpdatePictureState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean = false,
    val clearState: () -> Unit = {},
)
