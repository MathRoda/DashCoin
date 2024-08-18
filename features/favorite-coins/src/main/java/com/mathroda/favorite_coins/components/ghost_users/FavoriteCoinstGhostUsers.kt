package com.mathroda.favorite_coins.components.ghost_users

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.theme.DarkGray
import com.mathroda.favorite_coins.R

@ExperimentalLayoutApi
@Composable
fun WatchListGhostUsers(
    navigateToSignIn: () -> Unit
) {

    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.watch_list_anim))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        topBar = { CommonTopBar(title = "Favorite Coins") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(DarkGray)
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            if (lottieComp != null) {
            LottieAnimation(
                modifier = Modifier
                    .padding(top = 16.dp, end = 24.dp)
                    .graphicsLayer(scaleY = 0.6f, scaleX = 0.6f),
                composition = lottieComp,
                progress = { lottieProgress },
            )

            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = "Feature limited for registered users",
                style = com.mathroda.common.theme.Typography.body1,
                color = com.mathroda.common.theme.TextWhite.copy(alpha = 0.6f)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomLoginButton(
                    text = "LOGIN",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkGray)
                        .padding(bottom = 24.dp),
                ) {
                    navigateToSignIn()
                }
            }
                }
        }
    }
}