package com.farooq.sentinelai.ui.components



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun RealtimeChart(
    values: List<Float>,
    modifier: Modifier = Modifier
) {

    val chartEntries =
        values.mapIndexed { index, value ->
            index.toFloat() to value
        }

    Chart(
        chart = lineChart(),
        model = entryModelOf(*chartEntries.toTypedArray()),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}