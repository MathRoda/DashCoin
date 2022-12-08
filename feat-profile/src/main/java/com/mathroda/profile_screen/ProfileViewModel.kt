package com.mathroda.profile_screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.domain.DashCoinUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val providersRepository: ProvidersRepository
) : ViewModel() {

    private val _userCredential = MutableStateFlow(DashCoinUser())
    val userCredential = _userCredential.asStateFlow()

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState: State<UserState> = _authState

    private val _isUserImageLoading = mutableStateOf(false)
    val isUserImageLoading: State<Boolean> = _isUserImageLoading

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

    fun uiState() {
        viewModelScope.launch {
            providersRepository.userStateProvider(
              function = {}
            ).collect {userState ->
                when(userState) {
                    is UserState.UnauthedUser -> _authState.value = userState
                    is UserState.AuthedUser -> _authState.value = userState
                    is UserState.PremiumUser -> _authState.value = userState
                }
            }
        }
    }

    fun updateProfilePicture(bitmap: Bitmap) {
        val imageName = userCredential.value.userUid ?: UUID.randomUUID().toString()

        viewModelScope.launch {
            firebaseRepository.uploadImageToCloud(
                name = imageName,
                bitmap = bitmap
            ).collect { uploadResult ->
                when(uploadResult) {
                    is Resource.Loading -> {
                        _isUserImageLoading.value = true

                        Log.d("Resource", "Loading Upload...")
                    }
                    is Resource.Success -> {
                        _isUserImageLoading.value = false

                        Log.d("Resource", "Success Upload: ${uploadResult.data}")
                        val imageUrl = uploadResult.data ?: ""
                        firebaseRepository
                            .updateUserProfilePicture(imageUrl = imageUrl)
                            .collect { updateResult ->
                                when(updateResult) {
                                    is Resource.Loading -> {
                                        Log.d("Resource", "Loading Update...")
                                    }
                                    is Resource.Success -> {
                                        Log.d("Resource", "Success Update")
                                    }
                                    is Resource.Error -> {
                                        Log.d("Resource", "Error Update: ${uploadResult.message}")
                                    }
                                }
                            }
                    }
                    is Resource.Error -> {
                        _isUserImageLoading.value = false

                        Log.d("Resource", "Error Upload: ${uploadResult.message}")
                    }
                }
            }
        }
    }
}