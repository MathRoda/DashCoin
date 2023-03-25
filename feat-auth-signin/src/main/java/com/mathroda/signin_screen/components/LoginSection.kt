package com.mathroda.signin_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.GoogleSignInButton
import com.mathroda.common.theme.TextWhite

@Composable
fun LoginSection(
    customLoginButton: () -> Unit,
    googleSignInButton: () -> Unit,
    isEnabled: Boolean
) {

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
                customLoginButton()
            }

            Spacer(modifier = Modifier.height(8.dp))
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
            }
        }
    }
}