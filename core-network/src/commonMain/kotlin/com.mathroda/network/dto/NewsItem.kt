package com.mathroda.network.dto

import com.mathroda.domain.NewsDetail
import kotlinx.serialization.Serializable

@Serializable
data class NewsItem(
    val id: String,
    val searchKeyWords: List<String>?,
    val feedDate: Long?,
    val source: String?,
    val title: String?,
    val sourceLink: String?,
    val isFeatured: Boolean?,
    val imgUrl: String?,
    val reactionsCount: Map<String, Int> = emptyMap(),
    val reactions: List<String> = emptyList(),
    val shareURL: String?,
    val relatedCoins: List<String>?,
    val content: Boolean?,
    val link: String?,
    val bigImg: Boolean?,
    val description: String?
)

fun NewsItem.toNewsDetails() =
    NewsDetail(
        description = description ?: "",
        id = id,
        feedDate = feedDate ?: 0,
        imgURL = imgUrl ?: "",
        link = link ?: "",
        relatedCoins = relatedCoins ?: emptyList(),
        shareURL = shareURL ?: "",
        source = source ?: "",
        sourceLink = sourceLink ?: "",
        title = title ?: ""
    )