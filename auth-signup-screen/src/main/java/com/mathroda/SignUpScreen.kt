package com.mathroda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.common.components.BackStackButton
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.CustomTextField
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.util.Constants
import com.mathroda.domain.User
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun SignUpScreen(
    navigateToSignInScreen: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val signUpState = viewModel.signUp.collectAsState()


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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .requiredHeight(40.dp)
            ) {
                BackStackButton {
                    popBackStack()
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = Constants.CREATE_ACCOUNT,
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
                    text = Constants.SIGN_UP_TO_GET_STARTED,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                ) {}
            }


            Spacer(modifier = Modifier.height(60.dp))

            CustomTextField(
                text = userName,
                placeholder = "Username",
                isPasswordTextField = false,
                onValueChange = { userName = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid username",
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
                text = email,
                placeholder = "Email",
                isPasswordTextField = false,
                onValueChange = { email = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid email address",
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


            Spacer(modifier = Modifier.weight(0.2f))

            CustomLoginButton(
                text = "REGISTER",
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled,
                isLoading = isLoading
            ) {
                isError = email.isEmpty() || password.isEmpty() || userName.isEmpty()
                val user = User(
                    userName = userName,
                    email = email
                )
                viewModel.signUp(user, password)
            }

            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = "already have an account? ",
                    fontSize = 16.sp
                ) {}
                Spacer(modifier = Modifier.width(4.dp))
                CustomClickableText(
                    text = "Login",
                    color = Gold,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W500
                ) {
                    popBackStack()
                    navigateToSignInScreen()
                }
            }
        }
    }

    if (signUpState.value.isLoading) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }
    }


    if (signUpState.value.signUp != null) {
        SweetToastUtil.SweetSuccess(
            message = "Account created successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        LaunchedEffect(Unit) {
            val user = User(userName, email)
            viewModel.addUserCredential(user)
            popBackStack()
            navigateToSignInScreen()
        }
    }

    if (signUpState.value.error.isNotBlank()) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }

        val errorMsg = signUpState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
    }
}