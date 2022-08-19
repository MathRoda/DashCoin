package com.mathroda.dashcoin.presentation.signup_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import com.mathroda.dashcoin.presentation.signup_screen.state.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseUseCases: FirebaseUseCases
): ViewModel() {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp


   fun signUp(email: String, password: String) =
        viewModelScope.launch {
            firebaseUseCases.signUp(email, password).onEach { result ->
                when(result) {
                    is Resource.Success -> {
                       _signUp.emit(SignUpState(signUp = result.data))
                    }
                    is Resource.Error -> {
                        _signUp.emit(SignUpState(error = result.message?: "Unexpected Error"))
                    }
                    is Resource.Loading -> {
                        _signUp.emit( SignUpState(isLoading = true))
                    }
                }
            }.launchIn(viewModelScope)
        }


}