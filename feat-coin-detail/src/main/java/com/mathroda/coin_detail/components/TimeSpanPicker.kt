package com.mathroda.coin_detail.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite

@Composable
fun TimeRangePicker(
    modifier: Modifier = Modifier,
    onTimeSelected: (TimeRange) -> Unit = {}
) {
    val timeOptions = mapOf(
        TimeRange.ONE_DAY to "1D",
        TimeRange.ONE_WEEK to "1W",
        TimeRange.ONE_MONTH to "1M",
        TimeRange.ONE_YEAR to "1Y",
        TimeRange.ALL to "ALL"
    )

    val selectedTime = remember { mutableStateOf(TimeRange.ONE_DAY) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        timeOptions.map { timeRange ->
            TimeRangeChip(
                time = timeRange.value,
                timeRange = timeRange.key,
                state = selectedTime,
                onTimeRangeSelected = { onTimeSelected(timeRange.key) }
            )
        }

    }

}

@Composable
private fun TimeRangeChip(
    time: String,
    timeRange: TimeRange,
    state: MutableState<TimeRange>,
    onTimeRangeSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (state.value == timeRange) LighterGray else DarkGray,
                shape = RoundedCornerShape(8.dp)
            )
            .selectable(
                selected = state.value == timeRange,
                onClick = {
                    onTimeRangeSelected()
                    state.value = timeRange
                }
            ),
    ) {
        Text(
            text = time,
            color = TextWhite,
            modifier = Modifier.padding(8.dp)
        )
    }
}