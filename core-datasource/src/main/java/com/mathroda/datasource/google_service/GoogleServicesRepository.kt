package com.mathroda.datasource.google_service

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.mathroda.core.util.Response
import kotlinx.coroutines.flow.Flow

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>

interface GoogleServicesRepository {

    val isUserExist: Boolean

    fun oneTapSignInWithGoogle(): Flow<OneTapSignInResponse>

    fun firebaseSignInWithGoogle(googleCred: AuthCredential): Flow<SignInWithGoogleResponse>
}