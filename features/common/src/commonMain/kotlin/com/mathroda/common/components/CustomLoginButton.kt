package com.mathroda.common.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.theme.CustomGreen

@Composable
fun CustomLoginButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CustomGreen,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
    ) {
        Text(text = text, fontSize = 20.sp, color = Color.White)
    }
}