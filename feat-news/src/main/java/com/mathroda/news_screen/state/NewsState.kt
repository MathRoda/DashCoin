package com.mathroda.news_screen.state

import com.mathroda.domain.model.NewsDetail

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<NewsDetail> = emptyList(),
    val error: String = ""
)
