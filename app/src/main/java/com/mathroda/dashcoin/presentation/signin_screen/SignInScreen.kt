package com.mathroda.dashcoin.presentation.signin_screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.presentation.signin_screen.viewmodel.SignInViewModel
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomClickableText
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomTextField
import com.mathroda.dashcoin.presentation.signup_screen.viewmodel.SignUpViewModel
import com.mathroda.dashcoin.presentation.ui.theme.Gold
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navigate: () ->  Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
    viewModelTest: SignInViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val isUserExist = viewModel.isCurrentUserExist.collectAsState(initial = true)
    val sigInState = viewModelTest.signIn.collectAsState()

    LaunchedEffect(Unit) {
        if (isUserExist.value) {
            navigate()

        }
    }

    if (!isUserExist.value) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomClickableText(
                        text = Constants.WELCOME_DASH_COIN,
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
                        text = Constants.SIGN_IN_TO_ACCESS,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    ) {}
                }


                Spacer(modifier = Modifier.height(60.dp))

                CustomTextField(
                    text = email,
                    placeholder = "Email",
                    isPasswordTextField = false,
                    onValueChange = { email = it.trim() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    text = password,
                    placeholder = "Password",
                    isPasswordTextField = true,
                    onValueChange = { password = it }
                )

                Spacer(modifier = Modifier.weight(0.2f))

                CustomLoginButton(
                    text = "LOGIN",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModelTest.signIn(email, password)
                    isLoading = !isLoading
                }

                Spacer(modifier = Modifier.weight(0.4f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomClickableText(
                        text = "New user? " ,
                        fontSize = 18.sp
                    ) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomClickableText(
                        text = "Register",
                        color = Gold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    ) {

                    }
                }
            }

        }

        if (sigInState.value.isLoading) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        rememberCoroutineScope().launch {
            if (sigInState.value.signIn != null) {
                delay(700)
                navigate()
                Log.e("signUp", "Loop")
            }
        }
    }
}