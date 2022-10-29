package com.mathroda.dashcoin.presentation.signin_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.signin_screen.state.ResetPasswordState
import com.mathroda.dashcoin.presentation.signin_screen.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _signIn = MutableStateFlow(SignInState())
    val signIn = _signIn.asStateFlow()

    private val _resetPassword = MutableStateFlow(ResetPasswordState())
    val resetPassword = _resetPassword.asStateFlow()

    fun signIn(email: String, password: String) =
            firebaseRepository.signInWithEmailAndPassword(email, password).onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        _signIn.emit(SignInState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _signIn.emit(SignInState(signIn = result.data))
                    }
                    is Resource.Error -> {
                        _signIn.emit(SignInState(error = result.message?: "Unexpected error accrued"))
                    }
                }
            }.launchIn(viewModelScope)

    fun resetPasswordWithEmail(email: String) =
        firebaseRepository.resetPasswordWithEmail(email).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _resetPassword.emit(ResetPasswordState(isLoading = true))
                }
                is Resource.Success -> {
                    _resetPassword.emit(ResetPasswordState(signIn = result.data))
                }
                is Resource.Error -> {
                    _resetPassword.emit(ResetPasswordState(error = result.message?: "Unexpected error accrued"))
                }
            }
        }.launchIn(viewModelScope)

}