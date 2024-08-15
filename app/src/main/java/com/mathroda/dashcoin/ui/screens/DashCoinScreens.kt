@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)

package com.mathroda.dashcoin.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.mathroda.BasicOnboarding
import com.mathroda.BasicSignUpScreen
import com.mathroda.coin_detail.BasicCoinDetailScreen
import com.mathroda.coins_screen.BasicCoinScreen
import com.mathroda.common.R
import com.mathroda.core.destination.DashCoinDestinations
import com.mathroda.dashcoin.navigation.main.BottomBar
import com.mathroda.dashcoin.navigation.main.bottomBarAnimatedScroll
import com.mathroda.favorite_coins.WatchListScreen
import com.mathroda.forgot_password.BasicForgotPasswordScreen
import com.mathroda.news_screen.NewsScreen
import com.mathroda.profile_screen.settings.SettingsScreen
import com.mathroda.signin_screen.BasicSignIn
import kotlin.math.roundToInt

class OnboardingScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coinsScreen = rememberScreen(DashCoinDestinations.Coins)
        BasicOnboarding {
            navigator.replaceAll(coinsScreen)
        }
    }

}

class SignInScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val signUpScreen = rememberScreen(DashCoinDestinations.SignUp)
        val forgotPasswordScreen = rememberScreen(DashCoinDestinations.ForgotPassword)
        BasicSignIn(
            navigateToSignUpScreen = { navigator.push(signUpScreen) },
            navigateToForgotPassword = { navigator.push(forgotPasswordScreen) },
            popBackStack = { navigator.pop() }
        )
    }
}

class SignUpScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BasicSignUpScreen(
            navigateBack = { navigator.pop() }
        )
    }
}

class ForgotPasswordScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BasicForgotPasswordScreen(
            navigateBack = { navigator.pop() }
        )
    }
}

data object CoinsScreen: Tab {
    private fun readResolve(): Any = CoinsScreen
    override val options: TabOptions
        @Composable
        get()  {
            val icon = painterResource(id = R.drawable.ic_crypto)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Home",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        BasicCoinScreen(
            navigateToSignIn = { navigator.push(SignInScreen()) },
            navigateToSettings = { navigator.push(SettingsScreen()) },
            navigateToCoinDetails = { navigator.push(CoinDetailsScreen(it)) }
        )
    }

}

data class CoinDetailsScreen(val id: String): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        BasicCoinDetailScreen(
            coinId = id,
            popBackStack = navigator::pop
        )
    }
}

data object FavoriteCoinsScreen: Tab {
    private fun readResolve(): Any = CoinsScreen
    override val options: TabOptions
        @Composable
        get()  {
            val icon = painterResource(id = R.drawable.ic_heart)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Watch List",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WatchListScreen(
            navigateToSignIn = { navigator.push(SignInScreen())},
            navigateToCoinDetails = { navigator.push(CoinDetailsScreen(it)) }
        )
    }

}

data object NewsScreen: Tab {
    private fun readResolve(): Any = CoinsScreen
    override val options: TabOptions
        @Composable
        get()  {
            val icon = painterResource(id = R.drawable.ic_news)

            return remember {
                TabOptions(
                    index = 2u,
                    title = "News",
                    icon = icon
                )
            }
        }
    @Composable
    override fun Content() {
        NewsScreen()
    }
}

class SettingsScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SettingsScreen {
            navigator.pop()
        }
    }

}

class TabsScreen: Screen {
    @Composable
    override fun Content() {
        val bottomBarHeight = 56.dp
        val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

        Scaffold(
            modifier = Modifier.bottomBarAnimatedScroll(
                height = bottomBarHeight,
                offsetHeightPx = bottomBarOffsetHeightPx
            ),
            bottomBar = {
                BottomBar(
                    isVisible = true,
                    modifier = Modifier
                        .height(bottomBarHeight)
                        .offset {
                            IntOffset(x = 0, y = -bottomBarOffsetHeightPx.floatValue.roundToInt())
                        }
                )
            }
        ) { paddingValues ->
            Box {
                CurrentTab()
                Box(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

