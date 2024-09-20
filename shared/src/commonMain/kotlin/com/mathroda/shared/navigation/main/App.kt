package com.mathroda.shared.navigation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.offset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.shared.destination.Destinations
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun App(
    navController: NavHostController,
    startDestinations: Destinations
) {
    /**
     * bottom bar variables for nested scroll
     */
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }


    Scaffold(
        modifier = Modifier
            .bottomBarAnimatedScroll(
            offsetHeightPx = bottomBarOffsetHeightPx
        ),
        bottomBar = {
            BottomBar(
                navController = navController,
                state = bottomBarVisibility(navController),
                modifier = Modifier
                    .offset {
                        IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt())
                    }
            )
        }
    ) { paddingValues ->
        MainGraph(navController = navController, startDestinations = startDestinations)
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
            backgroundColor =LighterGray,
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
                            painter = painterResource(resource = screen.icon!!),
                            contentDescription = null
                        )
                    },

                    selected = currentRoute == screen.route,

                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().route ?: Destinations.CoinsNews.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    alwaysShowLabel = false,
                    selectedContentColor = TextWhite,
                    unselectedContentColor = TextWhite
                )
            }
        }
    }

}



@Composable
fun bottomBarVisibility(
    navController: NavController,
): MutableState<Boolean> {

    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        Destinations.CoinsScreen.route -> bottomBarState.value = true
        Destinations.FavoriteCoinsScreen.route -> bottomBarState.value = true
        Destinations.CoinsNews.route -> bottomBarState.value = true
        else -> bottomBarState.value = false
    }

    return bottomBarState
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

