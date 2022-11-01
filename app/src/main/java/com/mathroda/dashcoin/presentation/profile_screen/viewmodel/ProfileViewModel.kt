package com.mathroda.dashcoin.presentation.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.User
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel(){

    private val _userCredential =  MutableStateFlow(User())
    val userCredential =  _userCredential.asStateFlow()

    private var getUserJob: Job ?= null

    init {
        getUserCredential()
    }

    fun signOut() = firebaseRepository.signOut()

    private fun getUserCredential() {
        getUserJob?.cancel()
        getUserJob = firebaseRepository.getUserCredentials().onEach { result ->
            when(result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _userCredential.emit(result.data?: User())
                }
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }
}