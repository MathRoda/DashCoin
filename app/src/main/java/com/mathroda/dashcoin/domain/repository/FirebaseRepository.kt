package com.mathroda.dashcoin.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.CoinById
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): Flow<String>

    fun signUpWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun getCurrentUser(): Flow<Resource<FirebaseUser>>

    fun signOut()

    fun addCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>>

    fun deleteCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>>

    fun getCoinFavorite(): Flow<Resource<List<CoinById>>>
}