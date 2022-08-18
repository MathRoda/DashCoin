package com.mathroda.dashcoin.presentation.signin_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import com.mathroda.dashcoin.presentation.signin_screen.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseUseCases: FirebaseUseCases
): ViewModel() {

    private val _signIn = MutableStateFlow(SignInState())
    val signIn: StateFlow<SignInState> = _signIn


    fun signIn(email: String, password: String) =
            firebaseUseCases.signIn(email, password).onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        _signIn.emit(SignInState(isLoading = true))
                    }

                    is Resource.Success -> {
                        _signIn.emit(SignInState(signIn = result.data))
                    }

                    is Resource.Error -> {
                        _signIn.emit(SignInState(error = result.message))
                    }
                }
            }.launchIn(viewModelScope)

}