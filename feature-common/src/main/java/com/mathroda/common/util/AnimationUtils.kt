package com.mathroda.common.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

val enterTransition: EnterTransition by lazy {
     slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))
}

val exitTransition: ExitTransition by lazy {
    slideOutHorizontally(
        targetOffsetX = { -300 },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))
}

val popEnterTransition: EnterTransition by lazy {
    slideInHorizontally(
        initialOffsetX = { -300 },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))
}

val popExitTransition: ExitTransition by lazy {
    slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))
}

