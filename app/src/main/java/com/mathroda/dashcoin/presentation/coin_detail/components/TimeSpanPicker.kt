package com.mathroda.dashcoin.presentation.coin_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.dashcoin.domain.model.ChartTimeSpan

@Composable
fun TimeSpanPicker(
    modifier: Modifier = Modifier,
    selectedTimeSpan: ChartTimeSpan = ChartTimeSpan.TIMESPAN_1DAY,
    onTimeSpanSelected: (ChartTimeSpan) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        TimeSpanChip(
            time = "24H",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_1DAY
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_1DAY)
        }

        TimeSpanChip(
            time = "1W",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_1WEK
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_1WEK)
        }

        TimeSpanChip(
            time = "1M",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_1MONTH
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_1MONTH)
        }

        TimeSpanChip(
            time = "3M",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_3MONTHS
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_3MONTHS)
        }

        TimeSpanChip(
            time = "6M",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_6MONTHS
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_6MONTHS)
        }

        TimeSpanChip(
            time = "1Y",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_1YEAR
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_1YEAR)
        }

        TimeSpanChip(
            time = "ALL",
            isSelected = selectedTimeSpan == ChartTimeSpan.TIMESPAN_ALL
        ) {
            onTimeSpanSelected(ChartTimeSpan.TIMESPAN_ALL)
        }
    }
}

@Composable
fun TimeSpanChip(
    time: String,
    isSelected: Boolean,
    onTimeSpanSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.background,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onTimeSpanSelected
            }
    ) {
        Text(
            text = time,
            color = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(8.dp)
                )
    }
}