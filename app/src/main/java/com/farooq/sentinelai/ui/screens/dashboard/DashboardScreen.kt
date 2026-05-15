package com.farooq.sentinelai.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farooq.sentinelai.data.models.*
import com.farooq.sentinelai.ui.components.*
import com.farooq.sentinelai.ui.theme.*
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val cpuMetrics by viewModel.cpuMetrics.collectAsState()
    val ramMetrics by viewModel.ramMetrics.collectAsState()
    val batteryMetrics by viewModel.batteryMetrics.collectAsState()
    val networkMetrics by viewModel.networkMetrics.collectAsState()
    val healthScore by viewModel.healthScore.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()

    DashboardContent(
        cpuMetrics = cpuMetrics,
        ramMetrics = ramMetrics,
        batteryMetrics = batteryMetrics,
        networkMetrics = networkMetrics,
        healthScore = healthScore,
        aiInsights = aiInsights,
        cpuChartProducer = viewModel.cpuChartProducer,
        ramChartProducer = viewModel.ramChartProducer
    )
}

@Composable
fun DashboardContent(
    cpuMetrics: CpuMetrics?,
    ramMetrics: RamMetrics?,
    batteryMetrics: BatteryMetrics?,
    networkMetrics: NetworkMetrics?,
    healthScore: Int,
    aiInsights: List<String>,
    cpuChartProducer: ChartEntryModelProducer? = null,
    ramChartProducer: ChartEntryModelProducer? = null
) {
    Scaffold(
        containerColor = BackgroundBlack,
        topBar = { DashboardHeader() }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            CyberRadar(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = (-20).dp)
                    .size(280.dp),
                color = CyanAccent.copy(alpha = 0.15f)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    SystemHealthCenter(healthScore)
                }

                item {
                    AIInsightsPanel(aiInsights)
                }

                item {
                    Text(
                        text = "LIVE TELEMETRY HUB",
                        style = Typography.labelSmall,
                        color = CyanAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            MetricHudCard(
                                title = "CPU LOAD",
                                value = "${cpuMetrics?.usagePercentage?.toInt() ?: 0}%",
                                icon = Icons.Default.Memory,
                                color = NeonGreen,
                                modelProducer = cpuChartProducer,
                                modifier = Modifier.weight(1f)
                            )
                            MetricHudCard(
                                title = "RAM ADAPT",
                                value = "${ramMetrics?.usagePercentage?.toInt() ?: 0}%",
                                icon = Icons.Default.SettingsInputComponent,
                                color = CyanAccent,
                                modelProducer = ramChartProducer,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            MetricHudCard(
                                title = "ENERGY",
                                value = "${batteryMetrics?.percentage ?: 0}%",
                                icon = Icons.Default.BatteryChargingFull,
                                color = PurpleAccent,
                                modifier = Modifier.weight(1f)
                            )
                            MetricHudCard(
                                title = "NET SPEED",
                                value = "${networkMetrics?.downloadSpeedKbps?.toInt() ?: 0}",
                                icon = Icons.Default.Speed,
                                color = NeonGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    QuickActionsSection()
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun SystemHealthCenter(score: Int) {
    val animatedScore by animateIntAsState(targetValue = score, label = "score")
    
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "SYSTEM INTEGRITY",
                    style = Typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = "$animatedScore%",
                    style = Typography.displayLarge.copy(
                        fontSize = 54.sp,
                        color = if (score > 80) NeonGreen else if (score > 50) CyanAccent else Color.Red,
                        fontWeight = FontWeight.Black
                    )
                )
                Text(
                    text = if (score > 80) "OPTIMAL" else if (score > 50) "STABLE" else "CRITICAL",
                    style = Typography.bodyLarge,
                    color = if (score > 80) NeonGreen else if (score > 50) CyanAccent else Color.Red
                )
            }
            
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.size(80.dp),
                    color = if (score > 80) NeonGreen else CyanAccent,
                    strokeWidth = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = if (score > 80) NeonGreen else CyanAccent,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun AIInsightsPanel(insights: List<String>) {
    Column {
        Text(
            text = "AI CORE INSIGHTS",
            style = Typography.labelSmall,
            color = PurpleAccent,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = PurpleAccent.copy(alpha = 0.3f)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                insights.take(3).forEach { insight ->
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = PurpleAccent,
                            modifier = Modifier.size(16.dp).offset(y = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = insight,
                            style = Typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricHudCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modelProducer: ChartEntryModelProducer? = null,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderColor = color.copy(alpha = 0.5f)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, shape = CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = value,
                style = Typography.displayLarge.copy(fontSize = 32.sp, color = TextPrimary)
            )
            Text(
                text = title,
                style = Typography.labelSmall,
                color = color
            )

            if (modelProducer != null) {
                Spacer(modifier = Modifier.height(8.dp))
                GlowChart(
                    modelProducer = modelProducer,
                    lineColor = color,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "SENTINEL HUD",
                style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                letterSpacing = 2.sp
            )
            Text(
                text = "AI-POWERED TELEMETRY",
                style = Typography.labelSmall,
                color = CyanAccent
            )
        }
        
        IconButton(
            onClick = { },
            modifier = Modifier
                .clip(CircleShape)
                .background(GlassWhite)
        ) {
            Icon(Icons.Default.Tune, contentDescription = null, tint = TextPrimary)
        }
    }
}

@Composable
fun QuickActionsSection() {
    Column {
        Text(
            text = "SYSTEM OVERRIDES",
            style = Typography.labelSmall,
            color = NeonGreen,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NeonButton(
                text = "Optimize",
                icon = Icons.Default.Bolt,
                color = NeonGreen,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
            NeonButton(
                text = "Security",
                icon = Icons.Default.VerifiedUser,
                color = CyanAccent,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
fun DashboardPreview() {
    SentinelAITheme {
        DashboardContent(
            cpuMetrics = CpuMetrics(34f, 8, 2.4, emptyList()),
            ramMetrics = RamMetrics(4000L, 8000L, 4000L, 50f, emptyList()),
            batteryMetrics = BatteryMetrics(85, false, 32.5f, "GOOD", 4000),
            networkMetrics = NetworkMetrics(1250.0, 450.0, true, "192.168.1.1", emptyList()),
            healthScore = 92,
            aiInsights = listOf("Memory pressure critical", "Thermal alert detected")
        )
    }
}
