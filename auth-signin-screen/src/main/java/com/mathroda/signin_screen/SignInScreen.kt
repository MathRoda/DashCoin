package com.mathroda.signin_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.CustomTextField
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.util.Constants.SIGN_IN_TO_ACCESS
import com.mathroda.core.util.Constants.WELCOME_DASH_COIN
import com.mathroda.signin_screen.state.SignInState
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@Composable
fun SignInScreen(
    navigateToCoinsScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val sigInState = viewModel.signIn.collectAsState()

    Scaffold {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = {
                    popBackStack()
                    navigateToCoinsScreen()
                }) {
                    Icon(
                        tint = Color.White,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 1f
                            scaleY = 1f
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = WELCOME_DASH_COIN,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ) {}
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = SIGN_IN_TO_ACCESS,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                ) {}
            }


            Spacer(modifier = Modifier.height(60.dp))

            CustomTextField(
                text = email,
                placeholder = "Email",
                onValueChange = { email = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid email address",
                isPasswordTextField = false,
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                text = password,
                placeholder = "Password",
                isPasswordTextField = !isPasswordVisible,
                onValueChange = { password = it },
                isError = isError,
                errorMsg = "*Enter valid password",
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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

            Spacer(modifier = Modifier.weight(0.2f))

            CustomLoginButton(
                text = "LOGIN",
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled,
                isLoading = isLoading
            ) {
                isError = email.isEmpty() || password.isEmpty()
                viewModel.signIn(email, password)
            }

            Spacer(modifier = Modifier.weight(0.4f))
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


    if (sigInState.value.isLoading) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }
    }


    if (sigInState.value.signIn != null) {
        LaunchedEffect(Unit) {
            delay(800)
            navigateToCoinsScreen()
        }
    }


    if (sigInState.value.error.isNotBlank()) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }

        val errorMsg = sigInState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
        popBackStack()
    }
}
