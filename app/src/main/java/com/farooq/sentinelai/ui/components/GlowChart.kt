package com.farooq.sentinelai.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.farooq.sentinelai.ui.theme.CyanAccent
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun GlowChart(
    modelProducer: ChartEntryModelProducer,
    modifier: Modifier = Modifier,
    lineColor: Color = CyanAccent
) {
    Chart(
        chart = lineChart(
            lines = listOf(
                LineChart.LineSpec(
                    lineColor = lineColor.toArgb(),
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        Brush.verticalGradient(
                            listOf(lineColor.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
                )
            ),
            axisValuesOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = 100f)
        ),
        chartModelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}
