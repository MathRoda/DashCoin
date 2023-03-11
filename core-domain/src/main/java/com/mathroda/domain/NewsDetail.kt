package com.mathroda.domain

data class NewsDetail(
    val description: String,
    val id: String,
    val feedDate: Long,
    val imgURL: String,
    val link: String,
    val relatedCoins: List<Any>,
    val shareURL: String,
    val source: String,
    val sourceLink: String,
    val title: String
)
