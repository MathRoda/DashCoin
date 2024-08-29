package com.mathroda.common.toastmessage.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Model class used to hold success/error information (success/error), as well as trigger
 * success/error Message Bar (addSuccess/addError).
 * */
class MessageBarState {
    var success by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<Exception?>(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    internal var duration by mutableLongStateOf(0L)
        private set

    fun showSuccess(
        message: String,
        duration: Long = 3000L
    ) {
        error = null
        success = message
        updated = !updated
        this.duration = duration
    }

    fun showError(
        exception: Exception,
        duration: Long = 3000L
    ) {
        success = null
        error = exception
        updated = !updated
        this.duration = duration
    }
}