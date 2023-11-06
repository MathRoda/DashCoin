package com.mathroda.network.dto

import com.mathroda.domain.model.NewsDetail

data class NewsItem(
    val bigImg: Boolean,
    val coins: List<CoinX>,
    val content: Boolean,
    val description: String,
    val feedDate: Long,
    val id: String,
    val imgUrl: String,
    val isFeatured: Boolean,
    val link: String,
    val reactions: List<Any>,
    val reactionsCount: ReactionsCount,
    val relatedCoins: List<String>,
    val searchKeyWords: List<String>,
    val shareURL: String,
    val source: String,
    val sourceLink: String,
    val title: String
)

fun NewsItem.toNewsDetails() =
    NewsDetail(
        description = description,
        id = id,
        feedDate = feedDate,
        imgURL = imgUrl,
        link = link,
        relatedCoins = relatedCoins,
        shareURL = shareURL,
        source = source,
        sourceLink = sourceLink,
        title = title
    )