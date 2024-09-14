package com.mathroda.profile.drawer.state

data class UpdatePictureState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean = false,
)
