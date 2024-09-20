package com.mathroda.news.state

import com.mathroda.domain.NewsDetail

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<NewsDetail> = emptyList(),
    val error: String = ""
)
