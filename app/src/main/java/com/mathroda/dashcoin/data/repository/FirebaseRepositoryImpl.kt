package com.mathroda.dashcoin.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): FirebaseRepository {
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
            emit(Resource.Error(it.toString()))
        }
    }

    override fun isCurrentUserExist(): Flow<Boolean> {
        return flow {
            emit(firebaseAuth.currentUser != null)
        }
    }

    override fun getCurrentUser(): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            firebaseAuth.currentUser?.let {
                emit(Resource.Success(it))
            }
        }
    }

    override fun signOut() {
        return firebaseAuth.signOut()
    }

    override fun addCoinFavorite(coinById: CoinById): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId().collect {
                val favoriteRef = fireStore.collection(Constants.FAVOURITES_COLLECTION)
                    .document(it)
                    .collection("coins").document(coinById.name.orEmpty())
                    .set(coinById)

                favoriteRef.await()

                emit(Resource.Success(favoriteRef))
            }
        }.catch {
            emit(Resource.Error(it.toString()))
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

    override fun getCoinFavorite(): Flow<Resource<List<CoinById>>> {
        return flow {
            emit(Resource.Loading())
            getUserId().collect {
                val snapshot = fireStore.collection(Constants.FAVOURITES_COLLECTION)
                    .document(it)
                    .collection("coins").get().await()

                val data = snapshot.toObjects(CoinById::class.java)
                emit(Resource.Success(data))
            }
        }
    }
}