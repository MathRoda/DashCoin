package com.mathroda.dashcoin.presentation.news_screen.state

import com.mathroda.dashcoin.domain.model.NewsDetail

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<NewsDetail> = emptyList(),
    val error: String = ""
)
