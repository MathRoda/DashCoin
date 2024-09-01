package com.mathroda.datasource.firebase

import com.mathroda.core.util.Constants
import com.mathroda.core.util.Resource
import com.mathroda.domain.DashCoinUser
import com.mathroda.domain.FavoriteCoin
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FirebaseRepositoryImpl (
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : FirebaseRepository {

    companion object {
        const val TAG = "auth"
    }

    override fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }


    override fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<DashCoinUser?>> {
        return flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    firebaseAuth.createUserWithEmailAndPassword(email, password)?.user?.toDashCoinUser()
                )
            )
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            )
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                firebaseAuth.sendPasswordResetEmail(email)
                emit(Resource.Success(true))
            }catch (e: Exception) {
                throw e
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun isCurrentUserExist(): Flow<Boolean> {
        return flow {
            emit(firebaseAuth.currentUser != null)
        }
    }

    override fun isUserExist(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserEmail(): Flow<String> {
        return flow {
            firebaseAuth.currentUser?.email?.let {
                emit(it)
            }
        }
    }

    override suspend fun signOut() {
        return firebaseAuth.signOut()
    }

    override fun addCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                fireStore.collection(Constants.FAVOURITES_COLLECTION)
                        .document(userUid)
                        .collection("coins").document(coin.name)
                        .set(coin)

                emit(Resource.Success(true))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun addUserCredential(dashCoinUser: DashCoinUser): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val userRef = fireStore.collection(Constants.USER_COLLECTION)
                    .document(userUid)
                    .set(dashCoinUser)

                emit(Resource.Success(userRef))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun deleteCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let {
                fireStore.collection(Constants.FAVOURITES_COLLECTION)
                    .document(it)
                    .collection("coins").document(coin.name)
                    .delete()

                emit(Resource.Success(true))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override suspend fun isFavoriteState(coin: FavoriteCoin): FavoriteCoin? {
        return try {
            getUserId()?.let { userId ->
                val coinDocument = fireStore.collection(Constants.FAVOURITES_COLLECTION)
                    .document(userId)
                    .collection("coins").document(coin.name)

                coinDocument.snapshots
                    .map { snapshot -> snapshot.data<FavoriteCoin>() }
                    .catch { e -> throw e}
                    .firstOrNull()
            }

        }catch (e: Exception) {
            null
        }
    }

    override fun getFlowFavoriteCoins(): Flow<Resource<List<FavoriteCoin>>> {
        return flow {
            try {
                emit(Resource.Loading())
                getUserId()?.let { userId ->
                    val snapshot =
                        fireStore.collection(Constants.FAVOURITES_COLLECTION)
                            .document(userId)
                            .collection("coins")
                            .snapshots

                    snapshot
                        .map { query ->
                            query.documents.map { snapshot ->
                                snapshot.data<FavoriteCoin>()
                            }
                        }
                        .catch { e -> emit(Resource.Error(e.message ?: "Unexpected Message")) }
                        .collect { coins ->
                            emit(Resource.Success(coins))
                        }
                }
            }catch (e: FirebaseNetworkException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }
        }
    }

    override fun updateFavoriteMarketState(coin: FavoriteCoin): Flow<Resource<Unit>> {
        return flow {
            val exist = isUserExist()
            if (exist) {
                emit(Resource.Loading())
                getUserId()?.let {
                        fireStore.collection(Constants.FAVOURITES_COLLECTION)
                            .document(it)
                            .collection("coins").document(coin.name)
                            .update("priceChange1d" to coin.priceChanged1d)

                    emit(Resource.Success(Unit))
                }
            }

        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun updateUserToPremium(result: Boolean): Flow<Resource<Unit>> {
        return flow {
            getUserId()?.let { userUid ->
                emit(Resource.Loading())
                    val favoriteRef =
                        fireStore.collection(Constants.USER_COLLECTION)
                            .document(userUid)
                            .update("premium", result)

                    emit(Resource.Success(favoriteRef))

            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override suspend fun updateFavoriteCoinsCount(count: Int) {
        getUserId()?.let { userUid ->
            fireStore.collection(Constants.USER_COLLECTION)
                .document(userUid)
                .update("favoriteCoinsCount" to count)

        }
    }

    override fun getUserCredentials(): Flow<Resource<DashCoinUser>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userId ->
                val snapShot =
                    fireStore.collection(Constants.USER_COLLECTION)
                        .document(userId)
                        .snapshots

               snapShot
                    .map { snapshot -> snapshot.data<DashCoinUser>() }
                    .catch { e -> emit(Resource.Error(e.message ?: "Unexpected Message")) }
                    .collect { user -> emit(Resource.Success(user))}
            }
        }
    }

    override fun uploadImageToCloud(
        name: String,
        bitmap: ByteArray
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            /* val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef = storageRef.child("images/$name.jpeg")

            try {
                // Upload the image bytes
                val uploadTask = imageRef.putData(Data())

                // Get the download URL after upload is complete
                val downloadUrl = imageRef.downloadURL.await()

                // Check if the download URL is valid
                if (downloadUrl == null) {
                    emit(Resource.Error("Invalid image URL"))
                } else {
                    emit(Resource.Success(downloadUrl.toString()))
                }
            } catch (e: Exception) {
                // Handle exceptions during upload and emit error
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }

        }.catch {
            emit(Resource.Error(it.message ?: "An error occurred"))
        }*/
        }
    }

    override fun updateUserProfilePicture(
        imageUrl: String
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userId ->
                fireStore.collection(Constants.USER_COLLECTION)
                    .document(userId)
                    .update("image" to imageUrl)

                emit(Resource.Success(Unit))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    }

    override suspend fun removeAllFavoriteCoins() {
        val userId = getUserId() ?: return
        fireStore.collection(Constants.FAVOURITES_COLLECTION)
            .document(userId)
            .delete()
    }

}

internal fun FirebaseUser.toDashCoinUser(): DashCoinUser {
    return DashCoinUser(
        userUid = this.uid,
        userName = this.displayName,
        email = this.email,
        image = this.photoURL
    )
}
