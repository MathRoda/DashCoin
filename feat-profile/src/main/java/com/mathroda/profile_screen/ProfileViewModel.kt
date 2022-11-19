package com.mathroda.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _userCredential = MutableStateFlow(User())
    val userCredential = _userCredential.asStateFlow()

    private var getUserJob: Job? = null

    init {
        getUserCredential()
    }

    fun signOut() = firebaseRepository.signOut()

    private fun getUserCredential() {
        getUserJob?.cancel()
        getUserJob = firebaseRepository.getUserCredentials().onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Loading -> {}
                is com.mathroda.core.util.Resource.Success -> {
                    result.data?.let {
                        _userCredential.emit(it)
                    }
                }
                is com.mathroda.core.util.Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }
}