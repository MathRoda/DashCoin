package com.mathroda.signin_screen

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.DashCoinTextField
import com.mathroda.common.components.LoadingDots
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.util.Constants.SIGN_IN_TO_ACCESS
import com.mathroda.core.util.Constants.WELCOME_DASH_COIN
import com.mathroda.signin_screen.components.LoginSection
import com.mathroda.signin_screen.components.OneTapSignIn
import com.mathroda.signin_screen.components.SignInWithGoogle
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@Composable
fun SignInScreen(
    navigateToCoinsScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    popBackStack: () -> Unit,
) {
    val viewModel = hiltViewModel<SignInViewModel>()

    val screenState by viewModel.screenState.collectAsState()
    val sigInState = viewModel.signIn.collectAsState()

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = {
                        popBackStack()
                        navigateToCoinsScreen()
                    }
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                CustomClickableText(
                    text = WELCOME_DASH_COIN,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ) {}

                Spacer(modifier = Modifier.height(5.dp))

                CustomClickableText(
                    text = SIGN_IN_TO_ACCESS,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                ) {}
            }

            Spacer(modifier = Modifier.height(60.dp))

            DashCoinTextField(
                text = screenState.email,
                placeholder = "Email",
                onValueChange = viewModel::updateEmailState,
                isError = screenState.isError,
                errorMsg = "*Enter valid email address",
                isPasswordTextField = false,
                singleLine = true,
                trailingIcon = {
                    if (screenState.email.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.updateEmailState("") }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashCoinTextField(
                text = screenState.password,
                placeholder = "Password",
                isPasswordTextField = !screenState.isPasswordVisible,
                onValueChange = viewModel::updatePasswordState,
                isError = screenState.isError,
                singleLine = true,
                errorMsg = "*Enter valid password",
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.updateIsPasswordVisible(!screenState.isPasswordVisible) }
                    ) {
                        Icon(
                            imageVector = if (screenState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            tint = Color.Gray,
                            contentDescription = "Password Toggle"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )

            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 4.dp),
                horizontalArrangement = Arrangement.End
            )
            {
                CustomClickableText(
                    text = "Forgot Password?",
                    color = Gold,
                    fontSize = 14.sp,
                ) {
                    navigateToForgotPassword()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LoadingDots(isLoading = screenState.isLoading)

            LoginSection(
                customLoginButton = viewModel::validatedSignIn,
                googleSignInButton = {
                    viewModel.oneTapSignIn()
                },
                isEnabled = screenState.isVisible
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = "New user? ",
                    fontSize = 18.sp
                ) {}
                Spacer(modifier = Modifier.width(4.dp))
                CustomClickableText(
                    text = "Register",
                    color = Gold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                ) {
                    navigateToSignUpScreen()
                }
            }
        }

    }

    if (sigInState.value.signIn != null) {
        SweetToastUtil.SweetSuccess(
            message = "You logged in successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        LaunchedEffect(Unit) {
            delay(800)
            navigateToCoinsScreen()
        }
    }


    if (sigInState.value.error.isNotBlank()) {

        val errorMsg = sigInState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
        popBackStack()
    }

    val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credentials = viewModel.onTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCred = getCredential(googleIdToken, null)
                viewModel.signInWithGoogle(googleCred)
            } catch (it: ApiException) {
                Log.e("TAG", it.message.toString())
            }
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignIn(
        launch = {
            launch(it)
        }
    )

    SignInWithGoogle(
        navigateToCoinsScreen = { signedIn ->
            if (signedIn) {
                navigateToCoinsScreen()
            }
        },
        updateScreenState = viewModel::updateIsVisibleIsLoadingState
    )
}
