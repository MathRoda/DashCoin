package com.mathroda.signup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.DashCoinUser
import com.mathroda.signup.state.SignUpState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository
) : ScreenModel {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp


    fun signUp(dashCoinUser: DashCoinUser, password: String) =
        screenModelScope.launch(Dispatchers.IO) {
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

        }.launchIn(screenModelScope)


}