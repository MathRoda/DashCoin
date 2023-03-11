package com.mathroda.signin_screen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.mathroda.core.util.Resource
import com.mathroda.core.util.Response
import com.mathroda.core.util.isValidEmail
import com.mathroda.core.util.isValidPassword
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.google_service.GoogleServicesRepository
import com.mathroda.datasource.google_service.OneTapSignInResponse
import com.mathroda.datasource.google_service.SignInWithGoogleResponse
import com.mathroda.signin_screen.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val googleServices: GoogleServicesRepository,
    val onTapClient: SignInClient
) : ViewModel() {

    private val _signIn = MutableStateFlow(SignInState())
    val signIn = _signIn.asStateFlow()

    private val _oneTapSignInResponse =
        MutableStateFlow<OneTapSignInResponse>(Response.Success(null))
    val oneTapSignInResponse = _oneTapSignInResponse.asStateFlow()

    private val _signInWithGoogleResponse =
        MutableStateFlow<SignInWithGoogleResponse>(Response.Success(false))
    val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()


    fun validatedSignIn(
        email: String,
        password: String,
        isError: MutableState<Boolean>
    ) {
        if (isValidEmail(email) && isValidPassword(password)) {
            signIn(email, password)
        } else {
            isError.value = !isValidEmail(email) || !isValidPassword(password)
        }
    }

    private fun signIn(email: String, password: String) =
        firebaseRepository.signInWithEmailAndPassword(email, password).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _signIn.emit(SignInState(isLoading = true))
                }
                is Resource.Success -> {
                    _signIn.emit(SignInState(signIn = result.data))
                }
                is Resource.Error -> {
                    _signIn.emit(
                        SignInState(
                            error = result.message ?: "Unexpected error accrued"
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

    fun oneTapSignIn() = viewModelScope.launch {
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


}