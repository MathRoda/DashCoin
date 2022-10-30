package com.mathroda.dashcoin.presentation.forgot_password.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.forgot_password.state.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _resetPassword = MutableStateFlow(ResetPasswordState())
    val resetPassword = _resetPassword.asStateFlow()

    fun resetPasswordWithEmail(email: String) =
        firebaseRepository.resetPasswordWithEmail(email).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _resetPassword.emit(ResetPasswordState(isLoading = true))
                }
                is Resource.Success -> {
                    _resetPassword.emit(ResetPasswordState(Successful = true))
                }
                is Resource.Error -> {
                    _resetPassword.emit(ResetPasswordState(error = result.message ?: "Unexpected error accrued"))
                }
            }
        }.launchIn(viewModelScope)
}