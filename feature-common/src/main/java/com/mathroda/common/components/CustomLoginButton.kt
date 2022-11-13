package com.mathroda.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.mathroda.common.R
import com.mathroda.common.theme.CustomBrightGreen
import com.mathroda.common.theme.CustomGreen

@Composable
fun CustomLoginButton(
    text: String,
    modifier: Modifier = Modifier,
    color: List<Color> = listOf(
        CustomGreen,
        CustomBrightGreen
    ),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_dots))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )

    AnimatedVisibility(visible = enabled) {
        Button(
            onClick = { onClick() },
            enabled = enabled,
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

    AnimatedVisibility(visible = isLoading) {
        LottieAnimation(
            modifier = modifier
                .size(150.dp),
            composition = lottieComp,
            progress = { lottieProgress },
        )
    }

}