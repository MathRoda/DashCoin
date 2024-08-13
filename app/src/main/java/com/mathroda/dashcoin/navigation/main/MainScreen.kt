package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathroda.common.components.bottomBarAnimatedScroll
import com.mathroda.common.components.bottomBarVisibility
import com.mathroda.common.navigation.Destinations
import kotlin.math.roundToInt

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    /**
     * bottom bar variables for nested scroll
     */
    val bottomBarHeight = 56.dp
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }


    Scaffold(
        modifier = Modifier.bottomBarAnimatedScroll(
            height = bottomBarHeight,
            offsetHeightPx = bottomBarOffsetHeightPx
        ),
        bottomBar = {
            BottomBar(
                navController = navController,
                state = bottomBarVisibility(navController),
                modifier = Modifier
                    .height(bottomBarHeight)
                    .offset {
                        IntOffset(x = 0, y = -bottomBarOffsetHeightPx.floatValue.roundToInt())
                    }
            )
        }
    ) { paddingValues ->
        MainGraph(navController = navController, paddingValues = paddingValues)
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        Destinations.CoinsScreen,
        Destinations.FavoriteCoinsScreen,
        Destinations.CoinsNews
    )

    AnimatedVisibility(
        visible = state.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            modifier = modifier,
            backgroundColor = com.mathroda.common.theme.LighterGray,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->

                BottomNavigationItem(
                    label = {
                        Text(text = screen.title!!)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon!!),
                            contentDescription = null
                        )
                    },

                    selected = currentRoute == screen.route,

                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    alwaysShowLabel = false,
                    selectedContentColor = com.mathroda.common.theme.TextWhite,
                    unselectedContentColor = com.mathroda.common.theme.TextWhite.copy(alpha = ContentAlpha.disabled)
                )
            }
        }
    }

}

