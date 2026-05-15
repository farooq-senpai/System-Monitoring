package com.farooq.sentinelai.data.repository

import com.farooq.sentinelai.analytics.AnalyticsEngine
import com.farooq.sentinelai.data.models.*
import com.farooq.sentinelai.monitoring.battery.BatteryMonitor
import com.farooq.sentinelai.monitoring.cpu.CPUMonitor
import com.farooq.sentinelai.monitoring.network.NetworkMonitor
import com.farooq.sentinelai.monitoring.ram.RAMMonitor
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelemetryRepository @Inject constructor(
    private val cpuMonitor: CPUMonitor,
    private val ramMonitor: RAMMonitor,
    private val batteryMonitor: BatteryMonitor,
    private val networkMonitor: NetworkMonitor,
    private val analyticsEngine: AnalyticsEngine
) {
    val cpuMetrics: Flow<CpuMetrics> = cpuMonitor.getCpuMetrics()
    val ramMetrics: Flow<RamMetrics> = ramMonitor.getRamMetrics()
    val batteryMetrics: Flow<BatteryMetrics> = batteryMonitor.getBatteryMetrics()
    val networkMetrics: Flow<NetworkMetrics> = networkMonitor.getNetworkMetrics()

    val systemStatus: Flow<SystemStatus> = combine(
        cpuMetrics, ramMetrics, batteryMetrics, networkMetrics
    ) { cpu, ram, battery, network ->
        SystemStatus(
            cpu = cpu,
            ram = ram,
            battery = battery,
            network = network,
            uptimeMillis = System.currentTimeMillis() // Simplification
        )
    }

    val healthScore: Flow<Int> = systemStatus.map { status ->
        analyticsEngine.calculateHealthScore(
            cpuUsage = status.cpu.usagePercentage,
            ramUsage = status.ram.usagePercentage,
            batteryTemp = status.battery.temperature,
            batteryHealth = status.battery.health
        )
    }

    val aiInsights: Flow<List<String>> = systemStatus.map { status ->
        analyticsEngine.generateInsights(
            cpu = status.cpu,
            ram = status.ram,
            battery = status.battery,
            network = status.network
        )
    }
}
