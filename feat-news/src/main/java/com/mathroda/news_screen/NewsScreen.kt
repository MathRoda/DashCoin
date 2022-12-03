package com.mathroda.news_screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.DarkGray
import com.mathroda.news_screen.components.NewsCard
import com.mathroda.news_screen.components.NewsFilterPicker
import com.mathroda.news_screen.components.NewsTopBar

enum class NewsFilter {
    HANDPICKED, TRENDING, LATEST, BULLISH, BEARISH
}

@ExperimentalMaterialApi
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state = viewModel.newsState.value
    val uriHandler = LocalUriHandler.current
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val isVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column {

            NewsTopBar(
                title = "Market News",
                isClicked = isVisible.value,
            ) {
                isVisible.value = it
            }

            NewsFilterPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                isVisible = isVisible.value
            ){ filter ->
                viewModel.getNews(filter)
            }

            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { viewModel.refresh() }) {

                    LazyColumn {
                        items(state.news) { news ->
                            NewsCard(
                                news = news
                            ) {
                                uriHandler.openUri(news.link)
                            }
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
