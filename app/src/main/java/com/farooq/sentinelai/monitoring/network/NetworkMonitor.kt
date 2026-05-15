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
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val history = mutableListOf<Double>()
    private val maxHistorySize = 30

    fun getNetworkMetrics(): Flow<NetworkMetrics> = flow {
        var lastRxBytes = TrafficStats.getTotalRxBytes()
        var lastTxBytes = TrafficStats.getTotalTxBytes()
        
        while (true) {
            delay(1000)
            val currentRxBytes = TrafficStats.getTotalRxBytes()
            val currentTxBytes = TrafficStats.getTotalTxBytes()
            
            // Speed in KB/s
            val downloadSpeed = (currentRxBytes - lastRxBytes) / 1024.0
            val uploadSpeed = (currentTxBytes - lastTxBytes) / 1024.0
            
            lastRxBytes = currentRxBytes
            lastTxBytes = currentTxBytes

            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

            val ipAddress = getIPAddress() ?: "127.0.0.1"

            history.add(downloadSpeed)
            if (history.size > maxHistorySize) history.removeAt(0)

            emit(
                NetworkMetrics(
                    downloadSpeedKbps = downloadSpeed,
                    uploadSpeedKbps = uploadSpeed,
                    isWifiConnected = isWifi,
                    ipAddress = ipAddress,
                    downloadHistory = history.toList()
                )
            )
        }
    }

    private fun getIPAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        val sAddr = address.hostAddress ?: continue
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (isIPv4) return sAddr
                    }
                }
            }
        } catch (_: Exception) {
            // ignore
        }
        return null
    }
}
