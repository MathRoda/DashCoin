package com.mathroda.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.util.Resource
import com.mathroda.core.util.isValidEmail
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.forgot_password.state.ForgotPasswordScreenState
import com.mathroda.forgot_password.state.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _resetPassword =
        MutableStateFlow(ResetPasswordState())
    val resetPassword = _resetPassword.asStateFlow()

    private val _screenState = MutableStateFlow(ForgotPasswordScreenState())
    val screenState = _screenState.asStateFlow()

    fun validateEmail() {
        val email = _screenState.value.email
        if (!isValidEmail(email)) {
            _screenState.update { it.copy(isError = true) }
            return
        }

        resetPasswordWithEmail(email)
    }

    private fun resetPasswordWithEmail(email: String) =
        firebaseRepository.resetPasswordWithEmail(email).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _resetPassword.emit(
                        ResetPasswordState(
                            isLoading = true
                        )
                    )
                    updateIsVisibleIsLoadingState(
                        isVisible = false,
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _resetPassword.emit(
                        ResetPasswordState(
                            Successful = true
                        )
                    )
                    updateIsVisibleIsLoadingState(
                        isVisible = false,
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _resetPassword.emit(
                        ResetPasswordState(
                            error = result.message ?: "Unexpected error accrued"
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

    fun updateEmailState(
        value: String
    ) {
        _screenState.update { it.copy(email = value.trim()) }
        resetErrorState()
    }

    private fun resetErrorState() = _screenState.update { it.copy(isError = false) }

    fun updateIsVisibleIsLoadingState(
        isVisible: Boolean,
        isLoading: Boolean
    ) = _screenState.update {
        it.copy(
            isVisible = isVisible,
            isLoading = isLoading
        )
    }
}