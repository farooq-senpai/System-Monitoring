package com.farooq.sentinelai.monitoring.ram

import android.app.ActivityManager
import android.content.Context
import com.farooq.sentinelai.data.models.RamMetrics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RAMMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val history = mutableListOf<Float>()
    private val maxHistorySize = 20

    fun getRamMetrics(): Flow<RamMetrics> = flow {
        val memoryInfo = ActivityManager.MemoryInfo()
        while (true) {
            activityManager.getMemoryInfo(memoryInfo)
            
            val total = memoryInfo.totalMem
            val available = memoryInfo.availMem
            val used = total - available
            val usagePercentage = (used.toFloat() / total.toFloat()) * 100f
            
            history.add(usagePercentage)
            if (history.size > maxHistorySize) history.removeAt(0)

            emit(
                RamMetrics(
                    usedBytes = used,
                    totalBytes = total,
                    availableBytes = available,
                    usagePercentage = usagePercentage,
                    history = history.toList()
                )
            )
            delay(2000)
        }
    }
}
