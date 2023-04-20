package com.mathroda.datasource.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mathroda.core.util.Constants
import com.mathroda.core.util.Resource
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException

class FirebaseRepositoryImpl constructor(
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
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
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
            ).await()

            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            this.trySend(Resource.Success(true))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    exception.message?.let {
                        trySend(Resource.Error(it))
                    }
                }
            awaitClose { this.cancel() }
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

    override fun signOut() {
        return firebaseAuth.signOut()
    }

    override fun addCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val favoriteRef =
                    fireStore.collection(Constants.FAVOURITES_COLLECTION)
                        .document(userUid)
                        .collection("coins").document(coin.name)
                        .set(coin)

                favoriteRef.await()

                emit(Resource.Success(true))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun addUserCredential(dashCoinUser: DashCoinUser): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val userRef = fireStore.collection(Constants.USER_COLLECTION)
                    .document(userUid)
                    .set(dashCoinUser)

                userRef.await()
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
                val favoriteRef =
                    fireStore.collection(Constants.FAVOURITES_COLLECTION)
                        .document(it)
                        .collection("coins").document(coin.name)
                        .delete()

                favoriteRef.await()
                emit(Resource.Success(true))
            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun isFavoriteState(coin: FavoriteCoin): FavoriteCoin? {
        var favoriteCoin: FavoriteCoin? = null
        getUserId()?.let { userId ->
            fireStore.collection(Constants.FAVOURITES_COLLECTION)
                .document(userId)
                .collection("coins").document(coin.name)
                .addSnapshotListener { value, error ->
                    error?.let {
                        //Todo: cache and handle errors here
                    }
                    value?.let {
                        val data = it.toObject(FavoriteCoin::class.java)
                        favoriteCoin = data
                    }
                }
        }

        return favoriteCoin
    }

    override fun getFlowFavoriteCoins(): Flow<Resource<List<FavoriteCoin>>> {
        return callbackFlow {
            try {
                this.trySend(Resource.Loading())
                getUserId()?.let { userId ->
                    val snapshot =
                        fireStore.collection(Constants.FAVOURITES_COLLECTION)
                            .document(userId)
                            .collection("coins")
                    snapshot.addSnapshotListener { value, error ->
                        error?.let {
                            this.close(it)
                        }

                        value?.let {
                            val data = value.toObjects(FavoriteCoin::class.java)
                            this.trySend(Resource.Success(data))
                        }
                    }
                }
            }catch (e: IOException) {
                trySend(Resource.Error("Couldn't reach server. Check your internet connection"))
                close(e)
            }
            awaitClose { this.cancel() }
        }
    }

    override fun updateFavoriteMarketState(coin: FavoriteCoin): Flow<Resource<Task<Void>>> {
        return flow {
            isCurrentUserExist().collect { exist ->
                if (exist) {
                    emit(Resource.Loading())
                    getUserId()?.let {
                        val favoriteRef =
                            fireStore.collection(Constants.FAVOURITES_COLLECTION)
                                .document(it)
                                .collection("coins").document(coin.name)
                                .update("priceChange1d", coin.priceChanged1d)

                        favoriteRef.await()
                        emit(Resource.Success(favoriteRef))
                    }
                }

            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun updateUserToPremium(result: Boolean): Flow<Resource<Task<Void>>> {
        return flow {
            getUserId()?.let { userUid ->
                emit(Resource.Loading())
                    val favoriteRef =
                        fireStore.collection(Constants.USER_COLLECTION)
                            .document(userUid)
                            .update("premium", result)

                    favoriteRef.await()
                    emit(Resource.Success(favoriteRef))

            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override suspend fun updateFavoriteCoinsCount(count: Int) {
        getUserId()?.let { userUid ->
            val favoriteRef =
                fireStore.collection(Constants.USER_COLLECTION)
                    .document(userUid)
                    .update("favoriteCoinsCount", count)

            favoriteRef.await()
        }
    }

    override fun getUserCredentials(): Flow<Resource<DashCoinUser>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            getUserId()?.let { userId ->
                val snapShot =
                    fireStore.collection(Constants.USER_COLLECTION)
                        .document(userId)
                snapShot.addSnapshotListener { value, error ->
                    error?.let {
                        this.trySend(Resource.Error(it.message.toString()))
                        this.close(it)
                    }

                    value?.let {
                        val data = value.toObject(DashCoinUser::class.java)
                        this.trySend(Resource.Success(data!!))
                    }
                }
            }
            awaitClose { this.cancel() }
        }
    }

    override fun uploadImageToCloud(
        name: String,
        bitmap: Bitmap
    ): Flow<Resource<String>> {
        return callbackFlow {
            this.trySend(Resource.Loading())

            val storageRef = firebaseStorage.reference
            val imageRef = storageRef.child("images/$name.jpeg")

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            val uploadTask = imageRef.putBytes(data)

            uploadTask
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            this.trySend(Resource.Error(it.message.toString()))
                            this.close(it)
                        }
                    }

                    imageRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        if (downloadUri == null) {
                            this.trySend(Resource.Error("Invalid image URL"))
                            this.close(null)
                        } else {
                            this.trySend(Resource.Success(downloadUri.toString()))
                        }
                    } else {
                        this.trySend(Resource.Error(task.exception?.message.toString()))
                        this.close(task.exception)
                    }
                }

            awaitClose { this.cancel() }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    }

    override fun updateUserProfilePicture(
        imageUrl: String
    ): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userId ->
                val imageRef = fireStore.collection(Constants.USER_COLLECTION)
                    .document(userId)
                    .update("image", imageUrl)

                imageRef.await()
                emit(Resource.Success(imageRef))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

}
