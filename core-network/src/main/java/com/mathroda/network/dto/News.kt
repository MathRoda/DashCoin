package com.mathroda.network.dto

data class News(
    val coins: List<Any>,
    val content: Boolean,
    val description: String,
    val feedDate: Long,
    val icon: String,
    val id: String,
    val imgURL: String,
    val link: String,
    val reactionsCount: ReactionsCount,
    val relatedCoins: List<Any>,
    val shareURL: String,
    val source: String,
    val sourceLink: String,
    val title: String
)

fun News.toNewsDetail(): com.mathroda.domain.NewsDetail {
    return com.mathroda.domain.NewsDetail(
        description = description,
        id = id,
        imgURL = imgURL,
        link = link,
        relatedCoins = relatedCoins,
        shareURL = shareURL,
        source = source,
        sourceLink = sourceLink,
        title = title
    )
}