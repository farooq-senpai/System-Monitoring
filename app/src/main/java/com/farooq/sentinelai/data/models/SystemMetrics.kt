package com.farooq.sentinelai.data.models

data class CpuMetrics(
    val usagePercentage: Float,
    val coreCount: Int,
    val frequencyGhz: Double,
    val history: List<Float> = emptyList()
)

data class RamMetrics(
    val usedBytes: Long,
    val totalBytes: Long,
    val availableBytes: Long,
    val usagePercentage: Float,
    val history: List<Float> = emptyList()
)

data class BatteryMetrics(
    val percentage: Int,
    val isCharging: Boolean,
    val temperature: Float,
    val health: String,
    val voltage: Int
)

data class NetworkMetrics(
    val downloadSpeedKbps: Double,
    val uploadSpeedKbps: Double,
    val isWifiConnected: Boolean,
    val ipAddress: String,
    val downloadHistory: List<Double> = emptyList()
)

data class SystemStatus(
    val cpu: CpuMetrics,
    val ram: RamMetrics,
    val battery: BatteryMetrics,
    val network: NetworkMetrics,
    val uptimeMillis: Long
)
