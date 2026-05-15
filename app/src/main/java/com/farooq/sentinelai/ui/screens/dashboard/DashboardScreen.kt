package com.farooq.sentinelai.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farooq.sentinelai.data.models.BatteryMetrics
import com.farooq.sentinelai.data.models.CpuMetrics
import com.farooq.sentinelai.data.models.NetworkMetrics
import com.farooq.sentinelai.data.models.RamMetrics
import com.farooq.sentinelai.ui.components.GlassCard
import com.farooq.sentinelai.ui.components.NeonButton
import com.farooq.sentinelai.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val cpuMetrics by viewModel.cpuMetrics.collectAsState()
    val ramMetrics by viewModel.ramMetrics.collectAsState()
    val batteryMetrics by viewModel.batteryMetrics.collectAsState()
    val networkMetrics by viewModel.networkMetrics.collectAsState()

    DashboardContent(
        cpuMetrics = cpuMetrics,
        ramMetrics = ramMetrics,
        batteryMetrics = batteryMetrics,
        networkMetrics = networkMetrics
    )
}

@Composable
fun DashboardContent(
    cpuMetrics: CpuMetrics?,
    ramMetrics: RamMetrics?,
    batteryMetrics: BatteryMetrics?,
    networkMetrics: NetworkMetrics?
) {
    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            DashboardHeader()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SystemStatusOverview()
            }

            item {
                Text(
                    text = "REAL-TIME MONITORING",
                    style = Typography.labelSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MetricCard(
                        title = "CPU",
                        value = "${cpuMetrics?.usagePercentage?.toInt() ?: 0}%",
                        subtitle = "${cpuMetrics?.coreCount ?: 0} CORES ACTIVE",
                        icon = Icons.Default.Memory,
                        color = NeonGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "RAM",
                        value = "${ramMetrics?.usagePercentage?.toInt() ?: 0}%",
                        subtitle = "FREE: ${ramMetrics?.availableBytes?.div(1024 * 1024) ?: 0} MB",
                        icon = Icons.Default.Storage,
                        color = CyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MetricCard(
                        title = "BATTERY",
                        value = "${batteryMetrics?.percentage ?: 0}%",
                        subtitle = if (batteryMetrics?.isCharging == true) "CHARGING" else "DISCHARGING",
                        icon = Icons.Default.BatteryChargingFull,
                        color = PurpleAccent,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "NETWORK",
                        value = "${networkMetrics?.downloadSpeedKbps?.toInt() ?: 0}",
                        subtitle = "KB/S DOWNLOAD",
                        icon = Icons.Default.Speed,
                        color = NeonGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                QuickActions()
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
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
                text = "SENTINEL DASHBOARD",
                style = Typography.headlineMedium,
                letterSpacing = 2.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(NeonGreen, shape = androidx.compose.foundation.shape.CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SYSTEM ONLINE",
                    style = Typography.labelSmall.copy(color = NeonGreen)
                )
            }
        }
        
        IconButton(onClick = { }) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = TextSecondary)
        }
    }
}

@Composable
fun SystemStatusOverview() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "SYSTEM HEALTH SCORE",
                style = Typography.labelSmall,
                color = TextSecondary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "98",
                    style = Typography.displayLarge.copy(fontSize = 48.sp, color = NeonGreen)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "OPTIMAL PERFORMANCE", style = Typography.bodyLarge)
                    Text(text = "All systems operational", style = Typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderColor = color
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = value, style = Typography.displayLarge.copy(fontSize = 28.sp))
            Text(text = title, style = Typography.labelSmall, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = Typography.bodyMedium, fontSize = 10.sp)
        }
    }
}

@Composable
fun QuickActions() {
    Column {
        Text(
            text = "QUICK ACTIONS",
            style = Typography.labelSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeonButton(
                text = "Scan",
                icon = Icons.Default.Security,
                color = CyanAccent,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            NeonButton(
                text = "Optimize",
                icon = Icons.Default.Bolt,
                color = NeonGreen,
                onClick = {},
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
            networkMetrics = NetworkMetrics(1250.0, 450.0, true, "192.168.1.1", emptyList())
        )
    }
}
