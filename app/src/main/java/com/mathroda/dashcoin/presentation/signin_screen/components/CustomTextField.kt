package com.mathroda.dashcoin.presentation.signin_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray

@Composable
fun CustomTextField(
    text: String,
    placeholder: String,
    isPasswordTextField: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = placeholder, color = Color.LightGray)},
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = CustomGreen,
            unfocusedBorderColor = LighterGray,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordTextField) PasswordVisualTransformation()
    else VisualTransformation.None
    )
}