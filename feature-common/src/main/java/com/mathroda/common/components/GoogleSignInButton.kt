package com.mathroda.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.mathroda.common.R
import com.mathroda.common.theme.LighterGray

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_dots))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )
    AnimatedVisibility(visible = enabled) {
        Button(
            modifier = modifier
                .padding(bottom = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LighterGray
            ),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            onClick = onClick
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_google_logo
                ),
                contentDescription = null
            )
            Text(
                text = "Sign in with Google",
                modifier = Modifier.padding(6.dp),
                fontSize = 16.sp
            )
        }
    }

}