package com.mathroda.dashcoin.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.mathroda.dashcoin.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): Flow<String>

    fun signUpWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun getCurrentUser(): Flow<Resource<FirebaseUser>>

    fun signOut()
}