package com.farooq.sentinelai.monitoring.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import com.farooq.sentinelai.data.models.NetworkMetrics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val history = mutableListOf<Double>()
    private val maxHistorySize = 20

    fun getNetworkMetrics(): Flow<NetworkMetrics> = flow {
        var lastRxBytes = TrafficStats.getTotalRxBytes()
        var lastTxBytes = TrafficStats.getTotalTxBytes()
        
        while (true) {
            delay(2000)
            val currentRxBytes = TrafficStats.getTotalRxBytes()
            val currentTxBytes = TrafficStats.getTotalTxBytes()
            
            val downloadSpeed = (currentRxBytes - lastRxBytes) / 1024.0 / 2.0 // KB/s
            val uploadSpeed = (currentTxBytes - lastTxBytes) / 1024.0 / 2.0 // KB/s
            
            lastRxBytes = currentRxBytes
            lastTxBytes = currentTxBytes

            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

            history.add(downloadSpeed)
            if (history.size > maxHistorySize) history.removeAt(0)

            emit(
                NetworkMetrics(
                    downloadSpeedKbps = downloadSpeed,
                    uploadSpeedKbps = uploadSpeed,
                    isWifiConnected = isWifi,
                    ipAddress = "192.168.1.1", // Simplified
                    downloadHistory = history.toList()
                )
            )
        }
    }
}
