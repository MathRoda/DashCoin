package com.mathroda.signin_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.mathroda.common.components.CustomLoginButton
import kotlinx.coroutines.launch

@Composable
fun LoginSection(
    customLoginButton: () -> Unit,
    googleSignInButton: () -> Unit,
    isEnabled: Boolean
) {

    val focusManger = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    AnimatedVisibility(visible = isEnabled) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomLoginButton(
                text = "LOGIN",
                modifier = Modifier.fillMaxWidth(),
            ) {
                scope.launch { focusManger.clearFocus() }
                customLoginButton()
            }

            //TODO: Fix implementation for google OAtuth Login on ios and android
            /*Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "--- OR ---",
                modifier = Modifier.padding(6.dp),
                fontSize = 12.sp,
                color = TextWhite.copy(0.2f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth()
            ) {
                googleSignInButton()
            }*/
        }
    }
}