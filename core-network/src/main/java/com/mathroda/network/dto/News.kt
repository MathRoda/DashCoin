package com.mathroda.network.dto

import com.mathroda.domain.model.NewsDetail

data class News(
    val coins: List<Any> = emptyList(),
    val content: Boolean = false,
    val description: String = "",
    val feedDate: Long = 0,
    val icon: String = "",
    val id: String = "",
    val imgURL: String = "",
    val link: String = "",
    val reactionsCount: ReactionsCount? = null,
    val relatedCoins: List<Any> = emptyList(),
    val shareURL: String = "",
    val source: String = "",
    val sourceLink: String = "",
    val title: String = ""
)

fun News.toNewsDetail(): NewsDetail {
    return NewsDetail(
        description = description,
        id = id,
        imgURL = imgURL,
        feedDate = feedDate,
        link = link,
        relatedCoins = relatedCoins,
        shareURL = shareURL,
        source = source,
        sourceLink = sourceLink,
        title = title
    )
}