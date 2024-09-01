@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.common.components

import KottieAnimation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `dash coin`.features.common.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    var animation by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        animation = Res.readBytes("files/loading_dots.json").decodeToString()
    }

    val lottieComp = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )

    val lottieProgress by animateKottieCompositionAsState(
        composition = lottieComp,
        iterations = KottieConstants.IterateForever,
    )

    AnimatedVisibility(visible = isLoading) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            KottieAnimation(
                modifier = modifier
                    .size(150.dp),
                composition = lottieComp,
                progress = { lottieProgress.progress },
            )
        }
    }
}