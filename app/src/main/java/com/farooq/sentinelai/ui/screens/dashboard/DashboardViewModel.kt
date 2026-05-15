package com.farooq.sentinelai.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farooq.sentinelai.data.models.*
import com.farooq.sentinelai.monitoring.battery.BatteryMonitor
import com.farooq.sentinelai.monitoring.cpu.CPUMonitor
import com.farooq.sentinelai.monitoring.network.NetworkMonitor
import com.farooq.sentinelai.monitoring.ram.RAMMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val cpuMonitor: CPUMonitor,
    private val ramMonitor: RAMMonitor,
    private val batteryMonitor: BatteryMonitor,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _cpuMetrics = MutableStateFlow<CpuMetrics?>(null)
    val cpuMetrics: StateFlow<CpuMetrics?> = _cpuMetrics

    private val _ramMetrics = MutableStateFlow<RamMetrics?>(null)
    val ramMetrics: StateFlow<RamMetrics?> = _ramMetrics

    private val _batteryMetrics = MutableStateFlow<BatteryMetrics?>(null)
    val batteryMetrics: StateFlow<BatteryMetrics?> = _batteryMetrics

    private val _networkMetrics = MutableStateFlow<NetworkMetrics?>(null)
    val networkMetrics: StateFlow<NetworkMetrics?> = _networkMetrics

    init {
        startMonitoring()
    }

    private fun startMonitoring() {
        viewModelScope.launch {
            cpuMonitor.getCpuMetrics().collect { _cpuMetrics.value = it }
        }
        viewModelScope.launch {
            ramMonitor.getRamMetrics().collect { _ramMetrics.value = it }
        }
        viewModelScope.launch {
            batteryMonitor.getBatteryMetrics().collect { _batteryMetrics.value = it }
        }
        viewModelScope.launch {
            networkMonitor.getNetworkMetrics().collect { _networkMetrics.value = it }
        }
    }
}
