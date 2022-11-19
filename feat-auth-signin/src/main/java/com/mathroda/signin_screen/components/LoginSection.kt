package com.mathroda.signin_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.GoogleSignInButton
import com.mathroda.common.theme.TextWhite

@Composable
fun ColumnScope.LoginSection(
    customLoginButton: () -> Unit,
    googleSignInButton: () -> Unit,
    isEnabled: Boolean
) {
    AnimatedVisibility(visible = isEnabled) {
        CustomLoginButton(
            text = "LOGIN",
            modifier = Modifier.fillMaxWidth(),
        ) {
            customLoginButton()
        }
    }

    Spacer(modifier = Modifier.weight(0.1f))

    AnimatedVisibility(visible = isEnabled) {

        Text(
            text = "--- OR ---",
            modifier = Modifier.padding(6.dp),
            fontSize = 12.sp,
            color = TextWhite.copy(0.2f)
        )
    }

    Spacer(modifier = Modifier.weight(0.1f))

    AnimatedVisibility(visible = isEnabled) {
        GoogleSignInButton(
            modifier = Modifier.fillMaxWidth()
        ) {
            googleSignInButton()
        }
    }

}