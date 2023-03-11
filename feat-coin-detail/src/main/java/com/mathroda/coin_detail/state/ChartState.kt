package com.mathroda.coin_detail.state

import com.github.mikephil.charting.data.Entry

data class ChartState(
    val isLoading: Boolean = false,
    val chart: List<Entry> = emptyList(),
    val error: String = ""
)
