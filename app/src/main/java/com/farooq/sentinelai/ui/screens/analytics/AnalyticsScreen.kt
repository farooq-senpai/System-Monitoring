package com.farooq.sentinelai.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farooq.sentinelai.ui.components.GlassCard
import com.farooq.sentinelai.ui.components.GlowChart
import com.farooq.sentinelai.ui.theme.*
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    AnalyticsScreenContent(
        cpuChartProducer = viewModel.cpuChartProducer,
        ramChartProducer = viewModel.ramChartProducer,
        networkChartProducer = viewModel.networkChartProducer
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
fun AnalyticsPreview() {
    val cpu = ChartEntryModelProducer(listOf(entryOf(0, 10), entryOf(1, 40), entryOf(2, 20), entryOf(3, 80)))
    val ram = ChartEntryModelProducer(listOf(entryOf(0, 50), entryOf(1, 55), entryOf(2, 60), entryOf(3, 58)))
    val net = ChartEntryModelProducer(listOf(entryOf(0, 100), entryOf(1, 1200), entryOf(2, 400), entryOf(3, 900)))
    
    SentinelAITheme {
        AnalyticsScreenContent(
            cpuChartProducer = cpu,
            ramChartProducer = ram,
            networkChartProducer = net
        )
    }
}

@Composable
fun AnalyticsScreenContent(
    cpuChartProducer: ChartEntryModelProducer,
    ramChartProducer: ChartEntryModelProducer,
    networkChartProducer: ChartEntryModelProducer
) {
    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ANALYTICS ENGINE",
                    style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    letterSpacing = 2.sp
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                AnalyticsDetailCard(
                    title = "CPU PERFORMANCE TREND",
                    color = NeonGreen,
                    modelProducer = cpuChartProducer
                )
            }

            item {
                AnalyticsDetailCard(
                    title = "RAM ALLOCATION LOG",
                    color = CyanAccent,
                    modelProducer = ramChartProducer
                )
            }

            item {
                AnalyticsDetailCard(
                    title = "NETWORK THROUGHPUT",
                    color = PurpleAccent,
                    modelProducer = networkChartProducer
                )
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "PREDICTIVE ANALYSIS",
                            style = Typography.labelSmall,
                            color = CyanAccent
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PredictionRow("Battery Drain", "Stable", NeonGreen)
                        PredictionRow("Memory Pressure", "Increasing", Color.Yellow)
                        PredictionRow("Thermal State", "Optimal", NeonGreen)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun AnalyticsDetailCard(
    title: String,
    color: Color,
    modelProducer: ChartEntryModelProducer
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = color.copy(alpha = 0.5f)
    ) {
        Column {
            Text(
                text = title,
                style = Typography.labelSmall,
                color = color
            )
            Spacer(modifier = Modifier.height(16.dp))
            GlowChart(
                modelProducer = modelProducer,
                lineColor = color,
                modifier = Modifier.height(180.dp)
            )
        }
    }
}

@Composable
fun PredictionRow(label: String, state: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = Typography.bodyMedium)
        Text(text = state, style = Typography.bodyMedium.copy(color = color, fontWeight = FontWeight.Bold))
    }
}
