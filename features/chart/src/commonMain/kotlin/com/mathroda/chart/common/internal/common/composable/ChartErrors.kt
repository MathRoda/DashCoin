package com.mathroda.chart.common.internal.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mathroda.chart.common.internal.TestTags
import com.mathroda.chart.common.style.ChartViewStyle

@Composable
internal fun ChartErrors(chartViewStyle: ChartViewStyle, errors: List<String>) {
    Column(
        modifier = chartViewStyle.modifierMain
            .padding(15.dp)
            .testTag(TestTags.CHART_ERROR)
    ) {
        errors.forEach { error ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.error,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp),
                text = "$error\n",
                color = MaterialTheme.colors.onError
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}