package com.mathroda.dashcoin.presentation.signup_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseUseCases: FirebaseUseCases
): ViewModel() {

    private val _signUp = MutableStateFlow<Resource<AuthResult>>(Resource.Loading())
    val signUp: StateFlow<Resource<AuthResult>> = _signUp

    /* private val _signUp = mutableStateOf(SignUpState())
    val signUp: State<SignUpState> = _signUp */

    val isCurrentUserExist = firebaseUseCases.isCurrentUserExist.invoke()

    fun signUp(email: String, password: String) =
        viewModelScope.launch {
            firebaseUseCases.signIn.invoke(email, password).collect {
                _signUp.emit(it)
            }
        }

   /* fun signUp(email: String, password: String) =
        viewModelScope.launch {
            firebaseUseCases.signUp.invoke(email, password).collect { result ->
                when(result) {
                    is Resource.Success -> {
                       _signUp.value = SignUpState(signUp = result.data)
                    }
                    is Resource.Error -> {
                        _signUp.value = SignUpState(error = result.message?: "Unexpected Error")
                    }
                    is Resource.Loading -> {
                        _signUp.value = SignUpState(isLoading = true)
                    }
                }
            }
        } */


}