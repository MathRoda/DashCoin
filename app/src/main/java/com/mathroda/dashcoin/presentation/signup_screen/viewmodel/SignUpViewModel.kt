package com.mathroda.dashcoin.presentation.signup_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.User
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.signup_screen.state.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp


   fun signUp(user: User, password: String) =
        viewModelScope.launch {
            firebaseRepository.signUpWithEmailAndPassword(user.email, password).onEach { result ->
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

    fun addUserCredential(user: User) =
        firebaseRepository.addUserCredential(user).onEach {  }.launchIn(viewModelScope)


}