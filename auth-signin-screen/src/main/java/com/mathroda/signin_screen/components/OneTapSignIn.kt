package com.mathroda.signin_screen.components

import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.mathroda.core.util.Response
import com.mathroda.signin_screen.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun OneTapSignIn(
    viewModel: SignInViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit,
) {
    when(val oneTapResponse = viewModel.oneTapSignInResponse.collectAsState().value) {
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> {
            oneTapResponse.data?.let {
                LaunchedEffect(it) {
                   launch(it)
                }
            }
        }
        is Response.Failure -> {
            LaunchedEffect(Unit) {
                Log.e("dashcoinone", oneTapResponse.e.toString() )
            }
        }

    }
}