package com.mathroda.signin_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.DashCoinTextField
import com.mathroda.common.components.LoadingDots
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.core.util.Constants.SIGN_IN_TO_ACCESS
import com.mathroda.core.util.Constants.WELCOME_DASH_COIN
import com.mathroda.signin_screen.components.LoginSection
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@Composable
fun BasicSignIn(
    viewModel: SignInViewModel,
    navigateToSignUpScreen: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    popBackStack: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    val sigInState by viewModel.signIn.collectAsState()
    val oneTapGoogleSignIn by viewModel.oneTapSignInResponse.collectAsState()
    val messageBarState = LocalMessageBar.current

    LaunchedEffect(oneTapGoogleSignIn) {
        if (oneTapGoogleSignIn) {
            popBackStack()
        }
    }

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
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = Color.Gray
                            )
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

    if (sigInState.signIn != null) {
        messageBarState.showSuccess(
            message = "You logged in successfully"
        )
        LaunchedEffect(Unit) {
            delay(1000)
            popBackStack()
        }
    }


    if (sigInState.error.isNotBlank()) {

        val errorMsg = sigInState.error
        messageBarState.showError(
            message = errorMsg
        )
    }
}
