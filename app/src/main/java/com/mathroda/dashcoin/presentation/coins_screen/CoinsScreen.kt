package com.mathroda.dashcoin.presentation.coins_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.WorkInfo
import com.airbnb.lottie.compose.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.dashcoin.R
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.presentation.coins_screen.components.CoinsItem
import com.mathroda.dashcoin.presentation.coins_screen.components.SearchBar
import com.mathroda.dashcoin.presentation.coins_screen.components.CoinsScreenTopBar
import com.mathroda.dashcoin.presentation.coins_screen.viewmodel.CoinsViewModel
import com.mathroda.dashcoin.presentation.profile_screen.DrawerNavigation
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.LightGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun CoinScreen(
    viewModel: CoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val searchCoin = remember { mutableStateOf(TextFieldValue("")) }
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_main))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
        )
    val onWorkerSuccess = viewModel.onSuccessWorker.observeAsState().value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val userEmail = viewModel.userEmail.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CoinsScreenTopBar(
            title = "Live Prices",
            onNavigationDrawerClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        ) },
        drawerContent = {
            DrawerNavigation(welcomeUser = userEmail.value)
        },
        drawerBackgroundColor = DarkGray,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerScrimColor = LightGray

    ) {
        Box(
            modifier = Modifier
                .background(DarkGray)
                .fillMaxSize()
        ) {
            Column {
                SearchBar(
                    hint = "Search...",
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = searchCoin
                )
                val isBeingSearched = searchCoin.value.text
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { viewModel.refresh() }) {

                    LazyColumn {
                        items(items = state.value.coins.filter {
                            it.name.contains(isBeingSearched, ignoreCase = true) ||
                                    it.id.contains(isBeingSearched, ignoreCase = true) ||
                                    it.symbol.contains(isBeingSearched, ignoreCase = true)
                        }, key = { it.id }) { coins ->
                            CoinsItem(
                                coins = coins,
                                onItemClick = {
                                    navController.navigate(Screens.CoinDetailScreen.route + "/${coins.id}")
                                }
                            )
                        }

                    }
                }

            }

            onWorkerSuccess?.let { listOfWorkInfo ->

                if (listOfWorkInfo.isEmpty()) {
                    return@let
                }
                val workInfo: WorkInfo = listOfWorkInfo[0]

                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    viewModel.marketStates(Constants.BITCOIN_ID)
                }
            }

            if (state.value.isLoading) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition = lottieComp,
                        progress = { lottieProgress },
                    )
                }
            }

            if (state.value.error.isNotEmpty()) {
                Text(
                    text = state.value.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }

}

