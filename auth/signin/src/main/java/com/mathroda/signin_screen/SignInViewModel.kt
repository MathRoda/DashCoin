package com.mathroda.signin_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mathroda.core.util.Resource
import com.mathroda.core.util.Response
import com.mathroda.core.util.isValidEmail
import com.mathroda.core.util.isValidPassword
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.google_service.GoogleServicesRepository
import com.mathroda.datasource.google_service.OneTapSignInResponse
import com.mathroda.datasource.google_service.SignInWithGoogleResponse
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.signin_screen.state.SignInScreenState
import com.mathroda.signin_screen.state.SignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val googleServices: GoogleServicesRepository,
    //val onTapClient: SignInClient,
    private val dashCoinUseCases: DashCoinUseCases,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _signIn = MutableStateFlow(SignInState())
    val signIn = _signIn.asStateFlow()

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState = _screenState.asStateFlow()

    private val _oneTapSignInResponse =
        MutableStateFlow<OneTapSignInResponse>(Response.Success(null))
    val oneTapSignInResponse = _oneTapSignInResponse.asStateFlow()

    private val _signInWithGoogleResponse =
        MutableStateFlow<SignInWithGoogleResponse>(Response.Success(false))
    val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()


    fun validatedSignIn() {
        val email = _screenState.value.email
        val password = _screenState.value.password

        if (isValidEmail(email) && isValidPassword(password)) {
            signIn(
                email = email,
                password = password
            )
        } else {
            _screenState.update {
                it.copy(
                    isError = true
                )
            }
        }
    }

    private fun signIn(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        firebaseRepository.signInWithEmailAndPassword(email, password).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    updateIsVisibleIsLoadingState(
                        isVisible = false,
                        isLoading = true
                    )
                }
                is Resource.Success -> onSignInSuccess(result.data)
                is Resource.Error -> {
                    _signIn.emit(
                        SignInState(
                            error = result.message ?: "Unexpected error accrued"
                        )
                    )

                    updateIsVisibleIsLoadingState(
                        isVisible = true,
                        isLoading = false
                    )
                }
            }
        }
    }
    private suspend fun onSignInSuccess(
        data: Any?
    ) {
        _signIn.update { it.copy(signIn = data) }
        dataStoreRepository.saveIsUserExist(true)
        dashCoinUseCases.cacheDashCoinUser()
    }

    fun oneTapSignIn() = viewModelScope.launch(Dispatchers.IO) {
        googleServices.oneTapSignInWithGoogle().collect { result ->
            when (result) {
                is Response.Loading -> {
                    _oneTapSignInResponse.emit(Response.Loading)
                }
                is Response.Success -> {
                    _oneTapSignInResponse.emit(Response.Success(result.data))
                }
                is Response.Failure -> {
                    _oneTapSignInResponse.emit(Response.Failure(result.e))
                }
            }
        }
    }


    fun signInWithGoogle(googleCred: AuthCredential) = viewModelScope.launch {
        googleServices.firebaseSignInWithGoogle(googleCred).collect { result ->
            when (result) {
                is Response.Loading -> {
                    _signInWithGoogleResponse.emit(Response.Loading)
                }
                is Response.Success -> {
                    _signInWithGoogleResponse.emit(Response.Success(result.data))
                }
                is Response.Failure -> {
                    _signInWithGoogleResponse.emit(Response.Failure(result.e))
                }
            }

        }
    }


    fun updateEmailState(
        value: String
    ) {
        _screenState.update { it.copy(email = value.trim()) }
        resetErrorState()
    }

    fun updatePasswordState(
        value: String
    ) = _screenState.update { it.copy(password = value) }

    private fun resetErrorState()
        = _screenState.update { it.copy(isError = false) }

    fun updateIsPasswordVisible(
        value: Boolean
    ) = _screenState.update { it.copy(isPasswordVisible = value) }

    fun updateIsVisibleIsLoadingState(
        isVisible: Boolean,
        isLoading: Boolean
    ) = _screenState.update {
        it.copy(
            isVisible = isVisible,
            isLoading = isLoading
        )
    }
}