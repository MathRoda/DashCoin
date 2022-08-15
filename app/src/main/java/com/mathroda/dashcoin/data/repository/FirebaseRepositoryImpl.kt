package com.mathroda.dashcoin.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
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
          emit(Resource.Success(firebaseAuth.createUserWithEmailAndPassword(email, password).await()))
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
}