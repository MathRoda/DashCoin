package com.mathroda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.components.BackStackButton
import com.mathroda.common.components.CustomClickableText
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.DashCoinTextField
import com.mathroda.common.components.LoadingDots
import com.mathroda.common.theme.TextWhite
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.core.util.Constants
import kotlinx.coroutines.delay

@Composable
fun BasicForgotPasswordScreen(
    viewModel: ResetPasswordViewModel,
    navigateBack: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    val state = viewModel.resetPassword.collectAsState().value
    val messageBar = LocalMessageBar.current
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
                    navigateBack()
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
                text = screenState.email,
                placeholder = "Email ID",
                onValueChange = viewModel::updateEmailState,
                isError = screenState.isError,
                errorMsg = "*Enter valid email address",
                isPasswordTextField = false,
                trailingIcon = {
                    if (screenState.email.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateEmailState("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingDots(isLoading = screenState.isLoading)

            AnimatedVisibility(visible = screenState.isVisible) {
                CustomLoginButton(
                    text = "SUBMIT",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    viewModel.validateEmail()
                }
            }
        }

        when {
            state.isLoading -> {}

            state.successful -> {
                messageBar.showSuccess(
                    message = "Email Sent Successfully"
                )

                LaunchedEffect(Unit) {
                    delay(800)
                    navigateBack()
                }
            }

            state.error.isNotBlank() -> {
                val errorMsg = state.error
                messageBar.showError(
                    message = errorMsg
                )
            }
        }
    }
}