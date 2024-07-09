package com.mathroda.datasource.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.firebase.FirebaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun providesFirebaseApp(
        @ApplicationContext context: Context
    ) = FirebaseApp.initializeApp(context)

    @Provides
    @Singleton
    fun providesFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseAuth, fireStore, firebaseStorage)
    }

}