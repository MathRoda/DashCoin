@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.favorites.components.ghost_users

import KottieAnimation
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.resources.Res
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.common.theme.Typography
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@ExperimentalResourceApi
@ExperimentalLayoutApi
@Composable
fun WatchListGhostUsers(
    navigateToSignIn: () -> Unit
) {
    var animation by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        animation = Res.readBytes("files/watch_list_anim.json").decodeToString()
    }

    val lottieComp = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )
    val lottieProgress by animateKottieCompositionAsState(
        composition = lottieComp,
        iterations = KottieConstants.IterateForever
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
            KottieAnimation(
                modifier = Modifier
                    .padding(top = 16.dp, end = 24.dp)
                    .graphicsLayer(scaleY = 0.6f, scaleX = 0.6f),
                composition = lottieComp,
                progress = { lottieProgress.progress },
            )

            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = "Feature limited for registered users",
                style = Typography.body1,
                color = TextWhite.copy(alpha = 0.6f)
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