package com.mathroda.datasource.firebase

import com.mathroda.core.util.Resource
import com.mathroda.domain.DashCoinUser
import com.mathroda.domain.FavoriteCoin
import dev.gitlive.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): String?

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<DashCoinUser?>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun isUserExist(): Boolean

    fun getCurrentUserEmail(): Flow<String>

    suspend fun signOut()

    fun addCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>>

    fun addUserCredential(dashCoinUser: DashCoinUser): Flow<Resource<Unit>>

    fun deleteCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>>

    suspend fun isFavoriteState(coin: FavoriteCoin): FavoriteCoin?

    fun getFlowFavoriteCoins(): Flow<Resource<List<FavoriteCoin>>>

    fun updateFavoriteMarketState(coin: FavoriteCoin): Flow<Resource<Unit>>

    fun updateUserToPremium(result: Boolean): Flow<Resource<Unit>>

    suspend fun updateFavoriteCoinsCount(count: Int)

    fun getUserCredentials(): Flow<Resource<DashCoinUser>>

    fun uploadImageToCloud(name: String, bitmap: ByteArray): Flow<Resource<String>>

    fun updateUserProfilePicture(imageUrl: String): Flow<Resource<Unit>>

    suspend fun removeAllFavoriteCoins()
}