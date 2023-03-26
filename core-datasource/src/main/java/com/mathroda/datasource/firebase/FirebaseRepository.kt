package com.mathroda.datasource.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.mathroda.core.util.Resource
import com.mathroda.domain.CoinById
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getUserId(): String?

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

    fun isUserExist(): Boolean

    fun getCurrentUserEmail(): Flow<String>

    fun signOut()

    fun addCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun addUserCredential(dashCoinUser: com.mathroda.domain.DashCoinUser): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun deleteCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun isFavoriteState(coinById: CoinById): Flow<CoinById?>

    fun getCoinFavorite(): Flow<com.mathroda.core.util.Resource<List<CoinById>>>

    fun updateFavoriteMarketState(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>>

    fun updateUserToPremium(result: Boolean): Flow<Resource<Task<Void>>>

    suspend fun updateFavoriteCoinsCount(count: Int)

    fun getUserCredentials(): Flow<com.mathroda.core.util.Resource<com.mathroda.domain.DashCoinUser>>

    fun uploadImageToCloud(name: String, bitmap: Bitmap): Flow<Resource<String>>

    fun updateUserProfilePicture(imageUrl: String): Flow<Resource<Task<Void>>>
}