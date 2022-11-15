package com.mathroda.signin_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.mathroda.core.util.Resource
import com.mathroda.core.util.Response
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.google_service.GoogleServicesRepository
import com.mathroda.datasource.google_service.OneTapSignInResponse
import com.mathroda.datasource.google_service.SignInWithGoogleResponse
import com.mathroda.signin_screen.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private val _oneTapSignInResponse = MutableStateFlow<OneTapSignInResponse>(Response.Loading)
    val oneTapSignInResponse = _oneTapSignInResponse.asStateFlow()

    private val _signInWithGoogleResponse = MutableStateFlow<SignInWithGoogleResponse>(Response.Loading)
    val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()




    fun signIn(email: String, password: String) =
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
            when(result) {
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
           when(result) {
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