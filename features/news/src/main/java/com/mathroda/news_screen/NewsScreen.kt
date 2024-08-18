package com.mathroda.news_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.common.theme.DarkGray
import com.mathroda.news_screen.components.NewsCard
import com.mathroda.news_screen.components.NewsFilterPicker
import com.mathroda.news_screen.components.NewsScreenState
import com.mathroda.news_screen.components.NewsTopBar
import org.koin.androidx.compose.koinViewModel

enum class NewsFilter {
    HANDPICKED, TRENDING, LATEST, BULLISH, BEARISH
}

@ExperimentalMaterialApi
@Composable
fun NewsScreen(
    viewModel: NewsViewModel
) {
    val state = viewModel.newsState.value
    val uriHandler = LocalUriHandler.current
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val isVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
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
            ) { filter ->
                viewModel.getNews(filter)
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = { viewModel.refresh() }) {

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    items(state.news) { news ->
                        Spacer(Modifier.height(8.dp))

                        NewsCard(
                            news = news
                        ) {
                            uriHandler.openUri(news.link)
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

        }

        NewsScreenState(viewModel = viewModel)
    }
}
