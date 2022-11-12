package com.mathroda.datasource.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mathroda.domain.CoinById
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : FirebaseRepository {

    companion object {
        const val TAG = "auth"
    }

    override fun getUserId(): Flow<String> {
        return flow {
            firebaseAuth.currentUser?.uid?.let {
                emit(it)
            }
        }
    }


    override fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<com.mathroda.core.util.Resource<AuthResult>> {
        return flow {
            emit(com.mathroda.core.util.Resource.Loading())
            emit(
                com.mathroda.core.util.Resource.Success(
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                )
            )
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.toString()))
        }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<com.mathroda.core.util.Resource<AuthResult>> {
        return flow {
            emit(com.mathroda.core.util.Resource.Loading())
            emit(
                com.mathroda.core.util.Resource.Success(
                    firebaseAuth.signInWithEmailAndPassword(
                        email,
                        password
                    ).await()
                )
            )
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun resetPasswordWithEmail(email: String): Flow<com.mathroda.core.util.Resource<Boolean>> {
        return callbackFlow {
            this.trySend(com.mathroda.core.util.Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            this.trySend(com.mathroda.core.util.Resource.Success(true))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    exception.message?.let {
                        trySend(com.mathroda.core.util.Resource.Error(it))
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
            emit(com.mathroda.core.util.Resource.Loading())
            getUserId().collect { userUid ->
                val favoriteRef =
                    fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                        .document(userUid)
                        .collection("coins").document(coinById.name.orEmpty())
                        .set(coinById)

                favoriteRef.await()

                emit(com.mathroda.core.util.Resource.Success(favoriteRef))
            }
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun addUserCredential(user: com.mathroda.domain.User): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            emit(com.mathroda.core.util.Resource.Loading())
            getUserId().collect { userUid ->
                val userRef = fireStore.collection(com.mathroda.core.util.Constants.USER_COLLECTION)
                    .document(userUid)
                    .set(user)

                userRef.await()
                emit(com.mathroda.core.util.Resource.Success(userRef))
            }
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun deleteCoinFavorite(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            emit(com.mathroda.core.util.Resource.Loading())
            getUserId().collect {
                val favoriteRef =
                    fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                        .document(it)
                        .collection("coins").document(coinById.name.orEmpty())
                        .delete()

                favoriteRef.await()
                emit(com.mathroda.core.util.Resource.Success(favoriteRef))
            }
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.toString()))
        }
    }

    override fun isFavoriteState(coinById: CoinById): Flow<CoinById?> {
        return callbackFlow {
            getUserId().collect { userId ->
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

    override fun getCoinFavorite(): Flow<com.mathroda.core.util.Resource<List<CoinById>>> {
        return callbackFlow {
            this.trySend(com.mathroda.core.util.Resource.Loading())
            getUserId().collect { userId ->
                val snapshot =
                    fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                        .document(userId)
                        .collection("coins")
                snapshot.addSnapshotListener { value, error ->
                    error?.let {
                        this.close(it)
                    }

                    value?.let {
                        val data = value.toObjects(CoinById::class.java)
                        this.trySend(com.mathroda.core.util.Resource.Success(data))
                    }
                }
            }
            awaitClose { this.cancel() }
        }
    }

    override fun updateFavoriteMarketState(coinById: CoinById): Flow<com.mathroda.core.util.Resource<Task<Void>>> {
        return flow {
            isCurrentUserExist().collect { exist ->
                if (exist) {
                    emit(com.mathroda.core.util.Resource.Loading())
                    getUserId().collect {
                        val favoriteRef =
                            fireStore.collection(com.mathroda.core.util.Constants.FAVOURITES_COLLECTION)
                                .document(it)
                                .collection("coins").document(coinById.name.orEmpty())
                                .update("priceChange1d", coinById.priceChange1d)

                        favoriteRef.await()
                        emit(com.mathroda.core.util.Resource.Success(favoriteRef))
                    }
                }

            }
        }.catch {
            emit(com.mathroda.core.util.Resource.Error(it.toString()))
        }
    }

    override fun getUserCredentials(): Flow<com.mathroda.core.util.Resource<com.mathroda.domain.User>> {
        return callbackFlow {
            this.trySend(com.mathroda.core.util.Resource.Loading())
            getUserId().collect { userId ->
                val snapShot =
                    fireStore.collection(com.mathroda.core.util.Constants.USER_COLLECTION)
                        .document(userId)
                snapShot.addSnapshotListener { value, error ->
                    error?.let {
                        this.trySend(com.mathroda.core.util.Resource.Error(it.message.toString()))
                        this.close(it)
                    }

                    value?.let {
                        val data = value.toObject(com.mathroda.domain.User::class.java)
                        this.trySend(com.mathroda.core.util.Resource.Success(data!!))
                    }
                }
            }
            awaitClose { this.cancel() }
        }
    }

}
