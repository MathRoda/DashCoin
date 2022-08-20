package com.mathroda.dashcoin.presentation.signin_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.dashcoin.presentation.ui.theme.CustomBrightGreen
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen

@Composable
fun CustomLoginButton(
    text: String,
    modifier: Modifier = Modifier,
    color: List<Color> = listOf(CustomGreen, CustomBrightGreen),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        enabled = enabled ,
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    colors = color
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .height(58.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Text(text = text, fontSize = 20.sp, color = Color.White)
    }
}