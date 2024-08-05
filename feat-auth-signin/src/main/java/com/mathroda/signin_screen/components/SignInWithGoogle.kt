package com.mathroda.signin_screen.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mathroda.core.util.Response
import com.mathroda.signin_screen.SignInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInWithGoogle(
    navigateToCoinsScreen: (signedIn: Boolean) -> Unit,
    updateScreenState: (isVisible: Boolean, isLoading: Boolean) -> Unit
) {
    val viewModel: SignInViewModel = koinViewModel()
    when (val signInWithGoogleResponse =
        viewModel.signInWithGoogleResponse.collectAsState().value) {
        is Response.Loading -> {
           updateScreenState(false, true)
        }
        is Response.Success -> signInWithGoogleResponse.data?.let { signedIn ->
            if (signedIn) navigateToCoinsScreen(true)

            if (!signedIn) navigateToCoinsScreen(false)
        }
        is Response.Failure -> {
            LaunchedEffect(Unit) {
                updateScreenState(true, false)
                Log.e("dashcoinfirebase", signInWithGoogleResponse.e.toString())
            }
        }
    }
}