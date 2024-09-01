package com.mathroda.dashcoin.navigation.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.mathroda.common.R
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.destination.DashCoinDestinations
import com.mathroda.dashcoin.ui.screens.CoinsScreen
import com.mathroda.dashcoin.ui.screens.FavoriteCoinsScreen
import com.mathroda.dashcoin.ui.screens.NewsScreen
import com.mathroda.dashcoin.ui.screens.OnboardingScreen
import com.mathroda.dashcoin.ui.screens.TabsScreen
import com.mathroda.dashcoin.ui.screens.coinDetailsScreen
import com.mathroda.dashcoin.ui.screens.coinsScreen
import com.mathroda.dashcoin.ui.screens.favoriteCoinsScreen
import com.mathroda.dashcoin.ui.screens.forgotPasswordScreen
import com.mathroda.dashcoin.ui.screens.newsScreen
import com.mathroda.dashcoin.ui.screens.onboardingScreen
import com.mathroda.dashcoin.ui.screens.settingsScreen
import com.mathroda.dashcoin.ui.screens.signInScreen
import com.mathroda.dashcoin.ui.screens.signUpScreen
import kotlin.math.roundToInt

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    startDestinations: DashCoinDestinations
) {
    TabNavigator(CoinsScreen) {
        if (startDestinations == DashCoinDestinations.Onboarding) {
            Navigator(screen = OnboardingScreen())
        } else {
            Navigator(TabsScreen())
        }
    }
}

@Composable
fun BottomBar(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val screens = listOf(
        CoinsScreen,
        FavoriteCoinsScreen,
        NewsScreen,
    )
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            modifier = modifier,
            backgroundColor = LighterGray,
        ) {
            screens.forEach { tab ->
                TabNavigationItem(
                    tab = tab
                )
            }
        }
    }

}


@Composable
private fun RowScope.TabNavigationItem(
    tab: Tab
) {
    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        label = { tab.options.title.let { Text(text = it) } },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = null
                )
            }
        },
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab  },
        alwaysShowLabel = false,
        selectedContentColor = TextWhite,
        unselectedContentColor = TextWhite.copy(alpha = ContentAlpha.disabled)
    )
}


/**
 * connect to the nested scroll system and listen to the scroll
 */

fun Modifier.bottomBarAnimatedScroll(
    height: Dp = 56.dp,
    offsetHeightPx: MutableState<Float>
): Modifier = composed {
    val bottomBarHeightPx = with(LocalDensity.current) {
        height.roundToPx().toFloat()
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = offsetHeightPx.value + delta
                offsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    this.nestedScroll(nestedScrollConnection)
}

