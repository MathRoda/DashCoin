package com.mathroda.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _resetPassword =
        MutableStateFlow(com.mathroda.forgot_password.state.ResetPasswordState())
    val resetPassword = _resetPassword.asStateFlow()

    fun resetPasswordWithEmail(email: String) =
        firebaseRepository.resetPasswordWithEmail(email).onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Loading -> {
                    _resetPassword.emit(
                        com.mathroda.forgot_password.state.ResetPasswordState(
                            isLoading = true
                        )
                    )
                }
                is com.mathroda.core.util.Resource.Success -> {
                    _resetPassword.emit(
                        com.mathroda.forgot_password.state.ResetPasswordState(
                            Successful = true
                        )
                    )
                }
                is com.mathroda.core.util.Resource.Error -> {
                    _resetPassword.emit(
                        com.mathroda.forgot_password.state.ResetPasswordState(
                            error = result.message ?: "Unexpected error accrued"
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
}