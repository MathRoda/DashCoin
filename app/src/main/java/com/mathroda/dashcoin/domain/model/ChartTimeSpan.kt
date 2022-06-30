package com.mathroda.dashcoin.domain.model

enum class ChartTimeSpan(val value: String) {
    TIMESPAN_1DAY("24h"),
    TIMESPAN_1WEK("1w"),
    TIMESPAN_1MONTH("1m"),
    TIMESPAN_3MONTHS("3m"),
    TIMESPAN_6MONTHS("6m"),
    TIMESPAN_1YEAR("1y"),
    TIMESPAN_ALL("all"),
}