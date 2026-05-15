package com.farooq.sentinelai.monitoring.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.farooq.sentinelai.data.models.BatteryMetrics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getBatteryMetrics(): Flow<BatteryMetrics> = flow {
        while (true) {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, intentFilter)

            batteryStatus?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val percentage = (level * 100 / scale.toFloat()).toInt()
                
                val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                status == BatteryManager.BATTERY_STATUS_FULL
                
                val temperature = it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f
                val voltage = it.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
                
                val healthInt = it.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
                val health = when (healthInt) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> "GOOD"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "OVERHEAT"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "DEAD"
                    else -> "STABLE"
                }

                emit(
                    BatteryMetrics(
                        percentage = percentage,
                        isCharging = isCharging,
                        temperature = temperature,
                        health = health,
                        voltage = voltage
                    )
                )
            }
            delay(5000)
        }
    }
}
