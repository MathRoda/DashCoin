package com.mathroda.signin_screen.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.mathroda.core.util.Response
import com.mathroda.signin_screen.SignInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OneTapSignIn(
    launch: (result: BeginSignInResult) -> Unit,
    viewModel: SignInViewModel
) {
    when (val oneTapResponse = viewModel.oneTapSignInResponse.collectAsState().value) {
        is Response.Loading -> {}
        is Response.Success -> {
            oneTapResponse.data?.let {
                LaunchedEffect(it) {
                    launch(it)
                }
            }
        }
        is Response.Failure -> {
            LaunchedEffect(Unit) {
                Log.e("dashcoinone", oneTapResponse.e.toString())
            }
        }

    }
}