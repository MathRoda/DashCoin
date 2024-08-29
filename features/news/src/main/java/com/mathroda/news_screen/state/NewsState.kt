package com.mathroda.news_screen.state

import com.example.shared.NewsDetail

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<com.example.shared.NewsDetail> = emptyList(),
    val error: String = ""
)
