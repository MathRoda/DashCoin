package com.mathroda.datasource.google_service

import com.mathroda.domain.GoogleUser

interface GoogleAuthUiProvider {

    /**
     * Opens Sign In with Google UI,
     * @return returns GoogleUser
     */
    suspend fun signIn(): GoogleUser?
}

interface GoogleAuthProvider {
    fun getUiProvider(): GoogleAuthUiProvider

    suspend fun signOut()
}