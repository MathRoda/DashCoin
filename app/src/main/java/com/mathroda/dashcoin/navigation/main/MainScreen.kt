package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathroda.dashcoin.navigation.graphs.MainGraph
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        Screens.CoinDetailScreen.route + "/{coinId}" -> bottomBarState.value = false
        else -> bottomBarState.value = true
    }
    /**
     * bottom bar variables for nested scroll
     */
    val bottomBarHeight = 56.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }

    /**
     * connect to the nested scroll system and listen to the scroll
     */
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Scaffold (
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        bottomBar ={
             BottomBar(
                 navController = navController,
                 state = bottomBarState,
                 modifier = Modifier
                     .height(bottomBarHeight)
                     .offset { IntOffset(x = 0, y= -bottomBarOffsetHeightPx.value.roundToInt()) }
             )

        }
            )
    {
        MainGraph(navController = navController)
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screens.CoinsScreen,
        Screens.CoinsWatchList,
        Screens.CoinsNews,
        Screens.Profile
    )

    AnimatedVisibility(
        visible = state.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ){
        BottomNavigation(
            modifier = modifier,
            backgroundColor = LighterGray,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->

                BottomNavigationItem(
                    label = {
                        Text(text = screen.title!!)
                    },
                    icon = {
                        Icon(imageVector = screen.icon!! , contentDescription = null)
                    },

                    selected = currentRoute == screen.route,

                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState =true
                        }
                    },

                    alwaysShowLabel = false,
                    selectedContentColor = TextWhite,
                    unselectedContentColor = TextWhite.copy(alpha = ContentAlpha.disabled)
                )
            }
        }
    }

}

