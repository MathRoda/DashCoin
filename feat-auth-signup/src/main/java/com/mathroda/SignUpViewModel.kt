package com.mathroda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.signup_screen.state.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository
) : ViewModel() {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp


    fun signUp(dashCoinUser: DashCoinUser, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.signUpWithEmailAndPassword(dashCoinUser.email!!, password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUp.emit(SignUpState(signUp = result.data))
                    }
                    is Resource.Error -> {
                        _signUp.emit(
                            SignUpState(
                                error = result.message ?: "Unexpected Error"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _signUp.emit(SignUpState(isLoading = true))
                    }
                }
            }
        }

    fun addUserCredential(dashCoinUser: DashCoinUser) =
        firebaseRepository.addUserCredential(dashCoinUser).onEach { result ->
            when (result) {
                is Resource.Success -> { dashCoinRepository.cacheDashCoinUser(dashCoinUser) }
                else -> {}
            }

        }.launchIn(viewModelScope)


}