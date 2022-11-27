package com.mathroda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.DashCoinUser
import com.mathroda.signup_screen.state.SignUpState
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
) : ViewModel() {

    private val _signUp = MutableStateFlow(com.mathroda.signup_screen.state.SignUpState())
    val signUp: StateFlow<com.mathroda.signup_screen.state.SignUpState> = _signUp


    fun signUp(dashCoinUser: DashCoinUser, password: String) =
        viewModelScope.launch {
            firebaseRepository.signUpWithEmailAndPassword(dashCoinUser.email!!, password).onEach { result ->
                when (result) {
                    is com.mathroda.core.util.Resource.Success -> {
                        _signUp.emit(SignUpState(signUp = result.data))
                    }
                    is com.mathroda.core.util.Resource.Error -> {
                        _signUp.emit(
                            com.mathroda.signup_screen.state.SignUpState(
                                error = result.message ?: "Unexpected Error"
                            )
                        )
                    }
                    is com.mathroda.core.util.Resource.Loading -> {
                        _signUp.emit(com.mathroda.signup_screen.state.SignUpState(isLoading = true))
                    }
                }
            }.launchIn(viewModelScope)
        }

    fun addUserCredential(dashCoinUser: DashCoinUser) =
        firebaseRepository.addUserCredential(dashCoinUser).onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Loading -> {}
                is com.mathroda.core.util.Resource.Success -> {}
                is com.mathroda.core.util.Resource.Error -> {}
            }

        }.launchIn(viewModelScope)


}