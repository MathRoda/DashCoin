package com.mathroda.datasource.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mathroda.core.util.Constants
import com.mathroda.core.util.Resource
import com.mathroda.domain.CoinById
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
    ): Flow<com.mathroda.core.util.Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    firebaseAuth.signInWithEmailAndPassword(
                        email,
                        password
                    ).await()
                )
            )
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun resetPasswordWithEmail(email: String): Flow<com.mathroda.core.util.Resource<Boolean>> {
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

    override fun addCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val favoriteRef =
                    fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                        .document(userUid)
                        .collection("coins").document(coinById.name.orEmpty())
                        .set(coinById)

                favoriteRef.await()

                emit(Resource.Success(favoriteRef))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun addUserCredential(dashCoinUser: com.mathroda.domain.DashCoinUser): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val userRef = fireStore.collection(com.mathroda.core.util.Constants.USER_COLLECTION)
                    .document(userUid)
                    .set(dashCoinUser)

                userRef.await()
                emit(Resource.Success(userRef))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun deleteCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let {
                val favoriteRef =
                    fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                        .document(it)
                        .collection("coins").document(coinById.name.orEmpty())
                        .delete()

                favoriteRef.await()
                emit(Resource.Success(favoriteRef))
            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun isFavoriteState(coinById: CoinById): Flow<CoinById?> {
        return callbackFlow {
            getUserId()?.let { userId ->
                fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                    .document(userId)
                    .collection("coins").document(coinById.name.orEmpty())
                    .addSnapshotListener { value, error ->
                        error?.let {
                            this.close(it)
                        }
                        value?.let {
                            val data = it.toObject(CoinById::class.java)
                            this.trySend(data)
                        }
                    }
            }
            awaitClose { this.cancel() }
        }
    }

    override fun getCoinFavorite(): Flow<Resource<List<CoinById>>> {
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
                            val data = value.toObjects(CoinById::class.java)
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

    override fun updateFavoriteMarketState(coinById: CoinById): Flow<Resource<Task<Void>>> {
        return flow {
            isCurrentUserExist().collect { exist ->
                if (exist) {
                    emit(Resource.Loading())
                    getUserId()?.let {
                        val favoriteRef =
                            fireStore.collection(Constants.FAVOURITES_COLLECTION)
                                .document(it)
                                .collection("coins").document(coinById.name)
                                .update("priceChange1d", coinById.priceChange1d)

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

    override fun getUserCredentials(): Flow<com.mathroda.core.util.Resource<com.mathroda.domain.DashCoinUser>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            getUserId()?.let { userId ->
                val snapShot =
                    fireStore.collection(com.mathroda.core.util.Constants.USER_COLLECTION)
                        .document(userId)
                snapShot.addSnapshotListener { value, error ->
                    error?.let {
                        this.trySend(Resource.Error(it.message.toString()))
                        this.close(it)
                    }

                    value?.let {
                        val data = value.toObject(com.mathroda.domain.DashCoinUser::class.java)
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
