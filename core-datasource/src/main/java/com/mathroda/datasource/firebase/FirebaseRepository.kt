package com.mathroda.datasource.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.mathroda.core.util.Resource
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): String?

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun isUserExist(): Boolean

    fun getCurrentUserEmail(): Flow<String>

    fun signOut()

    fun addCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>>

    fun addUserCredential(dashCoinUser: DashCoinUser): Flow<Resource<Task<Void>>>

    fun deleteCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>>

    fun isFavoriteState(coin: FavoriteCoin): FavoriteCoin?

    fun getCoinFavorite(): Flow<Resource<List<FavoriteCoin>>>

    fun updateFavoriteMarketState(coin: FavoriteCoin): Flow<Resource<Task<Void>>>

    fun updateUserToPremium(result: Boolean): Flow<Resource<Task<Void>>>

    suspend fun updateFavoriteCoinsCount(count: Int)

    fun getUserCredentials(): Flow<Resource<DashCoinUser>>

    fun uploadImageToCloud(name: String, bitmap: Bitmap): Flow<Resource<String>>

    fun updateUserProfilePicture(imageUrl: String): Flow<Resource<Task<Void>>>
}