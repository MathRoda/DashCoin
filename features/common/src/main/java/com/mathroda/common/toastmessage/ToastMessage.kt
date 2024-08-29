package com.mathroda.common.toastmessage

interface ToastMessage {
    val showSuccess: (
        message: String,
        duration: Long
    ) -> Unit

    val showError: (
        message: String,
        duration: Long
    ) -> Unit
}