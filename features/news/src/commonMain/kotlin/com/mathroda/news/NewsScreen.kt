package com.mathroda.news

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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.DarkGray
import com.mathroda.news.components.NewsCard
import com.mathroda.news.components.NewsFilterPicker
import com.mathroda.news.components.NewsScreenState
import com.mathroda.news.components.NewsTopBar

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
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pullRefresh(pullRefreshState)
            ) {
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

                PullRefreshIndicator(
                    refreshing = viewModel.newsState.value.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }

        NewsScreenState(viewModel = viewModel)
    }
}
