package com.mathroda.dashcoin.presentation.signin_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.signin_screen.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _signIn = MutableStateFlow(SignInState())
    val signIn: StateFlow<SignInState> = _signIn

    val isCurrentUserExist = firebaseRepository.isCurrentUserExist()

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
                        _signIn.emit(SignInState(error = result.message?: "Unexpected error"))
                    }
                }
            }.launchIn(viewModelScope)

}