package com.mathroda.favorite_coins.components.ghost_users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.CustomBrightGreen
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.DarkGray
import com.mathroda.favorite_coins.R

@Composable
fun WatchListGhostUsers(navController: NavController) {

    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.watch_list_anim))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .background(com.mathroda.common.theme.DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            CommonTopBar(title = "Watch List")

            LottieAnimation(
                modifier = Modifier
                    .padding(top = 16.dp)
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
                    color = listOf(
                        CustomGreen,
                        CustomBrightGreen
                    )
                ) {
                    navController.navigate(Destinations.SignIn.route)
                }
            }


        }
    }
}