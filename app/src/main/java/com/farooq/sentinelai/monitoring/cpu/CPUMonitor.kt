package com.farooq.sentinelai.monitoring.cpu

import com.farooq.sentinelai.data.models.CpuMetrics
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class CPUMonitor @Inject constructor() {
    
    private val history = mutableListOf<Float>()
    private val maxHistorySize = 20

    fun getCpuMetrics(): Flow<CpuMetrics> = flow {
        while (true) {
            // Real CPU monitoring on Android is restricted. 
            // We'll use a realistic simulation for the system-wide stats for Phase 1.
            val usage = (Random.nextFloat() * 40f + 10f) // 10% to 50%
            
            history.add(usage)
            if (history.size > maxHistorySize) history.removeAt(0)
            
            emit(
                CpuMetrics(
                    usagePercentage = usage,
                    coreCount = Runtime.getRuntime().availableProcessors(),
                    frequencyGhz = 2.4, // Simplified
                    history = history.toList()
                )
            )
            delay(1000)
        }
    }
}
