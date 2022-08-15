package com.mathroda.dashcoin.presentation.signin_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val firebaseUseCases: FirebaseUseCases
): ViewModel() {

    private val _signIn = MutableStateFlow<Resource<AuthResult>>(Resource.Loading())
    val signIn: StateFlow<Resource<AuthResult>> = _signIn

    fun signIn(email: String, password: String) =
        viewModelScope.launch {
            firebaseUseCases.signIn.invoke(email, password).collect {
                _signIn.emit(it)
            }
        }
}