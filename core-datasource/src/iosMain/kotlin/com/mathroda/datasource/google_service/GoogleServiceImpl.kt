package com.mathroda.datasource.google_service

import com.mathroda.domain.GoogleUser

internal class GoogleAuthUiProviderImpl: GoogleAuthUiProvider {
    override suspend fun signIn(): GoogleUser? {
        return null
    }

}

internal class GoogleAuthProviderImpl: GoogleAuthProvider {
    override fun getUiProvider(): GoogleAuthUiProvider {
        return GoogleAuthUiProviderImpl()
    }

    override suspend fun signOut() {

    }

}