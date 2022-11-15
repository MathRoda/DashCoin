package com.mathroda.signin_screen.components

import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.core.util.Response
import com.mathroda.signin_screen.SignInViewModel

@Composable
fun SignInWithGoogle(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToCoinsScreen: (signedIn: Boolean) -> Unit,
    isVisible: (Boolean) -> Unit,
    isLoading: (Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse.collectAsState().value) {
        is Response.Loading -> {}
        is Response.Success -> signInWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn){
                isVisible(true)
                isLoading(false)
                navigateToCoinsScreen(signedIn)
            }
        }
        is Response.Failure -> {
            LaunchedEffect(Unit) {
                isVisible(true)
                isLoading(false)
                Log.e("dashcoinfirebase", signInWithGoogleResponse.e.toString() )
            }
        }
    }
}