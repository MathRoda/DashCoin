package com.mathroda.dashcoin.presentation.signup_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.presentation.signup_screen.state.SignUpState
import com.mathroda.datasource.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp


   fun signUp(user: com.mathroda.domain.User, password: String) =
        viewModelScope.launch {
            firebaseRepository.signUpWithEmailAndPassword(user.email!!, password).onEach { result ->
                when(result) {
                    is com.mathroda.core.util.Resource.Success -> {
                       _signUp.emit(SignUpState(signUp = result.data))
                    }
                    is com.mathroda.core.util.Resource.Error -> {
                        _signUp.emit(SignUpState(error = result.message?: "Unexpected Error"))
                    }
                    is com.mathroda.core.util.Resource.Loading -> {
                        _signUp.emit( SignUpState(isLoading = true))
                    }
                }
            }.launchIn(viewModelScope)
        }

    fun addUserCredential(user: com.mathroda.domain.User) =
        firebaseRepository.addUserCredential(user).onEach { result ->
            when(result) {
                is com.mathroda.core.util.Resource.Loading -> {}
                is com.mathroda.core.util.Resource.Success -> {}
                is com.mathroda.core.util.Resource.Error -> {}
            }

        }.launchIn(viewModelScope)


}