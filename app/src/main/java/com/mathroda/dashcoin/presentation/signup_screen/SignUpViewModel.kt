package com.mathroda.dashcoin.presentation.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val firebaseUseCases: FirebaseUseCases
): ViewModel() {

    private val _signUp = MutableStateFlow<Resource<AuthResult>>(Resource.Loading())
    val signUp: StateFlow<Resource<AuthResult>> = _signUp

    val isCurrentUserExist = firebaseUseCases.isCurrentUserExist.invoke()

    fun signUp(email: String, password: String) =
        viewModelScope.launch {
            firebaseUseCases.signIn.invoke(email, password).collect {
                _signUp.emit(it)
            }
        }
}