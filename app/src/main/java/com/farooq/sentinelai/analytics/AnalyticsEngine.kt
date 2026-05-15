package com.farooq.sentinelai.analytics

import com.farooq.sentinelai.data.models.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Singleton
class AnalyticsEngine @Inject constructor() {

    /**
     * Calculates a System Health Score (0-100) based on weighted metrics.
     */
    fun calculateHealthScore(
        cpuUsage: Float,
        ramUsage: Float,
        batteryTemp: Float,
        batteryHealth: String
    ): Int {
        var score = 100f

        // CPU Weight: 30%
        // High CPU (over 80%) starts penalizing heavily
        if (cpuUsage > 70f) {
            score -= (cpuUsage - 70f) * 0.5f
        }

        // RAM Weight: 30%
        // High RAM (over 85%) starts penalizing
        if (ramUsage > 80f) {
            score -= (ramUsage - 80f) * 1.5f
        }

        // Battery Temp Weight: 20%
        // Normal temp is < 40C. Over 45C is critical.
        if (batteryTemp > 40f) {
            score -= (batteryTemp - 40f) * 2.0f
        }

        // Battery Health Weight: 20%
        if (batteryHealth != "GOOD") {
            score -= 15f
        }

        return max(0, min(100, score.toInt()))
    }

    /**
     * Detects anomalies in data streams.
     */
    fun detectAnomalies(
        history: List<Float>,
        currentValue: Float,
        thresholdMultiplier: Float = 2.0f
    ): Boolean {
        if (history.size < 5) return false
        
        val average = history.average().toFloat()
        val spikeThreshold = average * thresholdMultiplier
        
        return currentValue > spikeThreshold && currentValue > 20f // Ignore tiny spikes
    }

    /**
     * Generates AI-inspired performance insights.
     */
    fun generateInsights(
        cpu: CpuMetrics?,
        ram: RamMetrics?,
        battery: BatteryMetrics?,
        network: NetworkMetrics?
    ): List<String> {
        val insights = mutableListOf<String>()

        cpu?.let {
            if (it.usagePercentage > 85f) {
                insights.add("Critical CPU spike detected. Background processes may be unstable.")
            } else if (it.usagePercentage > 60f) {
                insights.add("Elevated CPU load. System performance might be impacted.")
            }
        }

        ram?.let {
            if (it.usagePercentage > 90f) {
                insights.add("Memory pressure critical. Low RAM availability detected.")
            }
        }

        battery?.let {
            if (it.temperature > 42f) {
                insights.add("Thermal alert: Device temperature is unusually high.")
            }
            if (it.percentage < 20 && !it.isCharging) {
                insights.add("Low energy state: Power saving optimizations recommended.")
            }
        }

        network?.let {
            if (it.downloadSpeedKbps > 5000) {
                insights.add("High network throughput detected. Monitoring active data streams.")
            }
        }

        if (insights.isEmpty()) {
            insights.add("All systems nominal. AI monitoring active.")
        }

        return insights
    }
}
