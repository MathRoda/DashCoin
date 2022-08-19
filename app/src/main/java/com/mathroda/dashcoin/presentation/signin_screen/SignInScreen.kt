package com.mathroda.dashcoin.presentation.signin_screen

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomTextFieldWithError
import com.mathroda.dashcoin.presentation.ui.theme.Gold
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navigateToCoinsScreen: () ->  Unit,
    navigateToSignUpScreen: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val isUserExist = viewModel.isCurrentUserExist.collectAsState(initial = true)
    val sigInState = viewModel.signIn.collectAsState()

    LaunchedEffect(Unit) {
        if (isUserExist.value) {
            navigateToCoinsScreen()
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
                    onValueChange = { email = it.trim() },
                    isError = isError,
                    errorMsg = "*Enter valid email address",
                    isPasswordTextField = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    text = password,
                    placeholder = "Password",
                    isPasswordTextField = true,
                    onValueChange = { password = it },
                    isError = isError,
                    errorMsg = "*Enter valid password",

                )

                Spacer(modifier = Modifier.weight(0.2f))

                CustomLoginButton(
                    text = "LOGIN",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    isError = email.isEmpty()
                    viewModel.signIn(email, password)
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
                         navigateToSignUpScreen()
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


            if (sigInState.value.signIn != null) {
               SweetToastUtil.SweetSuccess(
                    message = "Welcome Back ${sigInState.value.signIn?.user?.email}",
                    duration = Toast.LENGTH_LONG
                    )
                rememberCoroutineScope().launch {
                    delay(700)
                    navigateToCoinsScreen()
                }
            }


        if(sigInState.value.error.isNotBlank()) {
            popBackStack()
            val errorMsg = sigInState.value.error
            SweetToastUtil.SweetError(message = errorMsg)
        }
    }
}
