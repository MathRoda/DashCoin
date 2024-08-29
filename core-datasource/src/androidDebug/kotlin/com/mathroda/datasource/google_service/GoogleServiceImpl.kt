package com.mathroda.datasource.google_service

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.mathroda.domain.GoogleAuthCredentials
import com.mathroda.domain.GoogleUser

internal class GoogleAuthUiProviderImpl(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val credentials: GoogleAuthCredentials,
): GoogleAuthUiProvider {
    override suspend fun signIn(): GoogleUser? {
        return try {
            val credentialManager = credentialManager.getCredential(
                context = context,
                request = getCredentialRequest()
            ).credential
            getGoogleUserFromCredential(credentialManager)
        }catch (e: GetCredentialException) {
            null
        }catch (e: NullPointerException) {
            null
        }
    }

    private fun getGoogleUserFromCredential(credential: Credential): GoogleUser? {
        return when {
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    GoogleUser(
                        idToken = googleIdTokenCredential.idToken,
                        displayName = googleIdTokenCredential.displayName ?: "",
                        profilePicUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    )
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("GoogleService","GoogleAuthUiProvider Received an invalid google id token response: ${e.message}")
                    null
                }
            }

            else -> null
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption(serverClientId = credentials.serverId))
            .build()
    }

    private fun getGoogleIdOption(serverClientId: String): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(serverClientId)
            .build()
    }
}

internal class GoogleAuthProviderImpl(
    private val context: Context,
    private val credentials: GoogleAuthCredentials,
    private val credentialManager: CredentialManager,
): GoogleAuthProvider {
    override fun getUiProvider(): GoogleAuthUiProvider {
        return GoogleAuthUiProviderImpl(
            context, credentialManager, credentials
        )
    }

    override suspend fun signOut() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

}