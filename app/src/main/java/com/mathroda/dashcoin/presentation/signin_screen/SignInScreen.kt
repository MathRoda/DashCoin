package com.mathroda.dashcoin.presentation.signin_screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Start
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomClickableText
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomTextField
import com.mathroda.dashcoin.presentation.signin_screen.components.SheetScreen
import com.mathroda.dashcoin.presentation.signin_screen.viewmodel.SignInViewModel
import com.mathroda.dashcoin.presentation.ui.theme.Gold
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
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
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    val sigInState = viewModel.signIn.collectAsState()
    val scope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = { SheetScreen() }
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
                                contentDescription = "Password Toggle")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)

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
                        scope.launch {
                            if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.2f))

                CustomLoginButton(
                    text = "LOGIN",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEnabled
                ) {
                    isEnabled = false
                    isError = email.isEmpty() || password.isEmpty()
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                 verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    Modifier
                        .padding(top = 50.dp)
                )
            }
        }
    }


    if (sigInState.value.signIn != null) {
        SweetToastUtil.SweetSuccess(
            message = "Welcome ${sigInState.value.signIn?.user?.email}",
            duration = Toast.LENGTH_LONG,
            padding = PaddingValues(bottom = 24.dp)
        )

        LaunchedEffect(Unit) {
            navigateToCoinsScreen()
        }
    }


    if(sigInState.value.error.isNotBlank()) {
        isEnabled = true
        val errorMsg = sigInState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
        popBackStack()
    }
}
