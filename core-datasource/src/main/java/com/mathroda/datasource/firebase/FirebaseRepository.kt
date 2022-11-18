package com.mathroda.datasource.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.mathroda.domain.CoinById
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): Flow<String>

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<com.mathroda.core.util.Resource<AuthResult>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<com.mathroda.core.util.Resource<AuthResult>>

    fun resetPasswordWithEmail(email: String): Flow<com.mathroda.core.util.Resource<Boolean>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun istUserExist(): Boolean

    fun getCurrentUserEmail(): Flow<String>

    fun signOut()

    fun addCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun addUserCredential(user: com.mathroda.domain.User): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun deleteCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun isFavoriteState(coinById: CoinById): Flow<CoinById?>

    fun getAllFavoriteCoins(): Flow<com.mathroda.core.util.Resource<List<CoinById>>>

    fun updateFavoriteMarketState(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun getUserCredentials(): Flow<com.mathroda.core.util.Resource<com.mathroda.domain.User>>
}