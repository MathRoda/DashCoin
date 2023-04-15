package com.mathroda.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.common.components.BackStackButton
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.DashCoinTextField
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.util.Constants
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val state = viewModel.resetPassword.collectAsState().value

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .requiredHeight(40.dp)
            ) {
                BackStackButton {
                    navController.popBackStack()
                    navController.navigate(Destinations.SignIn.route)
                }
            }

            CustomClickableText(
                text = Constants.FORGOT_PASSWORD,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ) {}

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                CustomClickableText(
                    text = Constants.FORGOT_PASSWORD_DESCRIPTION,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                ) {}
            }

            Spacer(modifier = Modifier.height(60.dp))


            DashCoinTextField(
                text = email,
                placeholder = "Email ID",
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

            CustomLoginButton(
                text = "SUBMIT",
                modifier = Modifier.fillMaxWidth(),
            ) {
                isError = email.isEmpty()
                viewModel.resetPasswordWithEmail(email)
            }
        }

        when {
            state.isLoading -> {
                LaunchedEffect(Unit) {
                    isEnabled = !isEnabled
                    isLoading = !isLoading
                }
            }

            state.Successful -> {
                SweetToastUtil.SweetSuccess(
                    message = "Email Sent Successfully",
                    duration = Toast.LENGTH_LONG,
                    padding = PaddingValues(bottom = 24.dp)
                )

                LaunchedEffect(Unit) {
                    delay(800)
                    navController.popBackStack()
                    navController.navigate(Destinations.SignIn.route)
                }
            }

            state.error.isNotBlank() -> {
                LaunchedEffect(Unit) {
                    isEnabled = !isEnabled
                    isLoading = !isLoading
                }
                val errorMsg = state.error
                SweetToastUtil.SweetError(
                    message = errorMsg,
                    padding = PaddingValues(bottom = 24.dp)
                )
            }
        }
    }
}