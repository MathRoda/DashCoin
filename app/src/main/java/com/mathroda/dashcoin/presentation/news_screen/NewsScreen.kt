package com.mathroda.dashcoin.presentation.news_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.dashcoin.presentation.coins_screen.components.SearchBar
import com.mathroda.dashcoin.presentation.coins_screen.components.TopBar
import com.mathroda.dashcoin.presentation.news_screen.components.NewsCard
import com.mathroda.dashcoin.presentation.news_screen.viewmodel.NewsViewModel
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray

@ExperimentalMaterialApi
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchNews = remember { mutableStateOf(TextFieldValue("")) }
    val state = newsViewModel.newsState.value
    val uriHandler = LocalUriHandler.current
    val searchCoin = remember { mutableStateOf(TextFieldValue("")) }
    val isRefreshing by newsViewModel.isRefresh.collectAsState()

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column {
            TopBar(title = "Trending News")
            SearchBar(
                hint = "Search...",
                state = searchNews,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                val isBeingSearched = searchCoin.value.text
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { newsViewModel.refresh() }) {

                    LazyColumn {
                        items(state.news.filter {
                            it.title.contains(isBeingSearched, ignoreCase = true) ||
                                    it.description.contains(isBeingSearched, ignoreCase = true)
                        }, key = {it.title}) { news ->
                            NewsCard(
                                newsThumb = news.imgURL,
                                title = news.title,
                                onClick = {
                                    uriHandler.openUri(news.link)
                                }
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = CustomGreen
            )
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
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
