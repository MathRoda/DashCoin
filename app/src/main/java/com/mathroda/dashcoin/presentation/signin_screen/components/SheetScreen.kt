package com.mathroda.dashcoin.presentation.signin_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SheetScreen() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            var email by remember { mutableStateOf("") }
            var isError by remember { mutableStateOf(false) }
            var isEnabled by remember { mutableStateOf(true) }

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

            CustomLoginButton(
                text = "LOGIN",
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                isEnabled = false
                isError = email.isEmpty()
            }
        }
}