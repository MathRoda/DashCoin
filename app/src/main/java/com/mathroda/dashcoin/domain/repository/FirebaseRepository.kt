package com.mathroda.dashcoin.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): Flow<String>

    fun signUpWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Resource<AuthResult>>

    fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun getCurrentUserEmail(): Flow<String>

    fun signOut()

    fun addCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>>

    fun addUserCredential(user: User): Flow<Resource<Task<Void>>>

    fun deleteCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>>

    fun isFavoriteState(coinById: CoinById): Flow<CoinById?>

    fun getCoinFavorite(): Flow<Resource<List<CoinById>>>

    fun getUserCredentials(): Flow<Resource<User>>
}