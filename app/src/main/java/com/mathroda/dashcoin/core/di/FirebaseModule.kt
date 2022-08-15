package com.mathroda.dashcoin.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mathroda.dashcoin.data.repository.FirebaseRepositoryImpl
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.domain.use_case.FirebaseUseCases
import com.mathroda.dashcoin.domain.use_case.auth.signin.SignInUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signout.SignOutUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signup.IsCurrentUserUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signup.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesFirebaseRepository(firebaseAuth: FirebaseAuth): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesFirebaseUseCases(
        firebaseRepository: FirebaseRepository
    ): FirebaseUseCases {
        return FirebaseUseCases(
            signUp = SignUpUseCase(firebaseRepository),
            signOut = SignOutUseCase(firebaseRepository),
            signIn = SignInUseCase(firebaseRepository),
            isCurrentUserExist = IsCurrentUserUseCase(firebaseRepository)
        )
    }
}