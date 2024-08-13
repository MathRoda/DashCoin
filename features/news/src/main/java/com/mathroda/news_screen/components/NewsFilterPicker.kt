package com.mathroda.news_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.news_screen.NewsFilter

@Composable
fun NewsFilterPicker(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onFilterSelected: (NewsFilter) -> Unit,
) {

    val filterOptions = mapOf(
        NewsFilter.TRENDING to "Trending",
        NewsFilter.LATEST to "Latest",
        NewsFilter.BULLISH to "Bullish",
        NewsFilter.BEARISH to "Bearish"
    )

    val selectedFilter = rememberSaveable { mutableStateOf(NewsFilter.TRENDING) }

    AnimatedVisibility(visible = isVisible) {
        Row(
            modifier = modifier
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            filterOptions.map { filter ->
                NewsFilterChip(
                    title = filter.value,
                    filter = filter.key,
                    state = selectedFilter,
                    onFilterSelected = { onFilterSelected(filter.key) }
                )
            }
        }
    }
}

@Composable
private fun NewsFilterChip(
    title: String,
    filter: NewsFilter,
    state: MutableState<NewsFilter>,
    onFilterSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (state.value == filter) LighterGray else DarkGray,
                shape = RoundedCornerShape(8.dp)
            )
            .selectable(
                selected = state.value == filter,
                onClick = {
                    onFilterSelected()
                    state.value = filter
                }
            ),
    ) {
        Text(
            text = title,
            color = TextWhite,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(8.dp)
        )
    }
}