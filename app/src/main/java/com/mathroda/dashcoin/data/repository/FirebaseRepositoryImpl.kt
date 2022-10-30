package com.mathroda.dashcoin.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.model.User
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): FirebaseRepository {

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
            emit(Resource.Success(firebaseAuth.signInWithEmailAndPassword(email, password).await()))
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
                        task.isSuccessful -> {this.trySend(Resource.Success(true))}
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

    override fun addCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId().collect { userUid ->
                val favoriteRef = fireStore.collection(Constants.FAVOURITES_COLLECTION)
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

    override fun addUserCredential(user: User): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId().collect { userUid ->
                val userRef = fireStore.collection(Constants.USER_COLLECTION)
                    .document(userUid)
                    .set(user)

                userRef.await()
                emit(Resource.Success(userRef))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun deleteCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId().collect {
                val favoriteRef = fireStore.collection(Constants.FAVOURITES_COLLECTION)
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
            getUserId().collect { userId ->
                fireStore.collection(Constants.FAVOURITES_COLLECTION)
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
           this.trySend(Resource.Loading())
            getUserId().collect { userId ->
                val snapshot = fireStore.collection(Constants.FAVOURITES_COLLECTION)
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
            awaitClose { this.cancel() }
        }
    }

}
