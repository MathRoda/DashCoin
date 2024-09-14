@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.common.components

import KottieAnimation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mathroda.common.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@Composable
fun LoadingCrypto(
    modifier: Modifier = Modifier
) {
    var animation by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        animation = Res.readBytes("files/loading_main.json").decodeToString()
    }

    val lottieComp = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )

    val lottieProgress by animateKottieCompositionAsState(
        composition = lottieComp,
        iterations = KottieConstants.IterateForever,
    )

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        KottieAnimation(
            composition = lottieComp,
            progress = { lottieProgress.progress },
        )
    }
}