package com.mathroda.datasource.google_service

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mathroda.core.util.Constants.SIGN_IN_REQUEST
import com.mathroda.core.util.Constants.SIGN_UP_REQUEST
import com.mathroda.core.util.Constants.USER_COLLECTION
import com.mathroda.core.util.Response
import com.mathroda.domain.model.DashCoinUser
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class GoogleServicesRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore

) : GoogleServicesRepository {
    override val isUserExist = auth.currentUser != null

    override fun oneTapSignInWithGoogle() = flow {
        try {
            emit(Response.Loading)

            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            emit(Response.Success(signInResult))
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Response.Success(signUpResult))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }

    override fun firebaseSignInWithGoogle(googleCred: AuthCredential) = flow {
        try {
            emit(Response.Loading)
            val authResult = auth.signInWithCredential(googleCred).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFireStore()
            }
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun addUserToFireStore() {
        auth.currentUser?.apply {
            val user = toGoogleUser()
            val userRef = db.collection(USER_COLLECTION)
                .document(uid)
                .set(user)

            userRef.await()
        }
    }

    private fun FirebaseUser.toGoogleUser() = DashCoinUser(
        userName = displayName,
        image = photoUrl?.toString(),
        email = email
    )
}