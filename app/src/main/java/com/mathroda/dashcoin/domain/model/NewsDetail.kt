package com.mathroda.dashcoin.domain.model

data class NewsDetail(
    val description: String,
    val id: String,
    val imgURL: String,
    val link: String,
    val relatedCoins: List<Any>,
    val shareURL: String,
    val source: String,
    val sourceLink: String,
    val title: String
)
