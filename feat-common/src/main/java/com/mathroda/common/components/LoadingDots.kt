package com.mathroda.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.mathroda.common.R

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {

    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_dots))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )

    AnimatedVisibility(visible = isLoading) {
        LottieAnimation(
            modifier = modifier
                .size(150.dp),
            composition = lottieComp,
            progress = { lottieProgress },
        )
    }
}