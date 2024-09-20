@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mathroda.common.resources.Res
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun FavoriteCoinsAnimation(
    modifier: Modifier = Modifier
) {
    val lottieComp by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/watch_list_anim.json").decodeToString()
        )
    }

    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = Compottie.IterateForever
    )

    BoxWithConstraints {
        val screenWidth = maxWidth
        Image(
            modifier = modifier
                .size(
                    screenWidth * 0.75f
                ),
            painter = rememberLottiePainter(
                composition = lottieComp,
                progress = { lottieProgress }
            ),
            contentDescription = null
        )
    }
}