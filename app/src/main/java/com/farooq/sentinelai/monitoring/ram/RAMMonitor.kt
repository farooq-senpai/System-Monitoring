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
import kotlin.math.roundToInt

@Singleton
class RAMMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE)
                as ActivityManager

    private val history = ArrayDeque<Float>()

    companion object {
        private const val UPDATE_INTERVAL = 1000L
        private const val MAX_HISTORY_SIZE = 30

        // Helps create more realistic Android memory readings
        private const val ANDROID_MEMORY_BUFFER_PERCENT = 0.08f
    }

    fun getRamMetrics(): Flow<RamMetrics> = flow {

        val memoryInfo = ActivityManager.MemoryInfo()

        while (true) {

            activityManager.getMemoryInfo(memoryInfo)

            val totalBytes = memoryInfo.totalMem

            val availableBytes = memoryInfo.availMem

            // Android keeps some RAM reserved/cached
            val reservedSystemMemory =
                (totalBytes * ANDROID_MEMORY_BUFFER_PERCENT).toLong()

            // More realistic used RAM calculation
            var usedBytes =
                totalBytes -
                        availableBytes -
                        reservedSystemMemory

            // Prevent negative values
            if (usedBytes < 0) {
                usedBytes = 0
            }

            // More accurate usage %
            val usagePercentage =
                (
                        usedBytes.toDouble() /
                                totalBytes.toDouble()
                        ) * 100.0

            // Clamp to realistic range
            val finalPercentage =
                usagePercentage
                    .coerceIn(0.0, 100.0)
                    .roundToInt()
                    .toFloat()

            // Maintain fixed history size
            if (history.size >= MAX_HISTORY_SIZE) {
                history.removeFirst()
            }

            history.addLast(finalPercentage)

            emit(
                RamMetrics(
                    usedBytes = usedBytes,
                    totalBytes = totalBytes,
                    availableBytes = availableBytes,
                    usagePercentage = finalPercentage,
                    history = history.toList()
                )
            )

            delay(UPDATE_INTERVAL)
        }
    }
}