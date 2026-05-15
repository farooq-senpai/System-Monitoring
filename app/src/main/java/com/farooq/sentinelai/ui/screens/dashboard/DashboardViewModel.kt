package com.farooq.sentinelai.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farooq.sentinelai.data.models.*
import com.farooq.sentinelai.data.repository.TelemetryRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val telemetryRepository: TelemetryRepository
) : ViewModel() {

    val cpuMetrics: StateFlow<CpuMetrics?> = telemetryRepository.cpuMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val ramMetrics: StateFlow<RamMetrics?> = telemetryRepository.ramMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val batteryMetrics: StateFlow<BatteryMetrics?> = telemetryRepository.batteryMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val networkMetrics: StateFlow<NetworkMetrics?> = telemetryRepository.networkMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val healthScore: StateFlow<Int> = telemetryRepository.healthScore
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 100)

    val aiInsights: StateFlow<List<String>> = telemetryRepository.aiInsights
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Chart Model Producers for Sparklines/Small Charts
    val cpuChartProducer = ChartEntryModelProducer()
    val ramChartProducer = ChartEntryModelProducer()

    init {
        observeMetricsForCharts()
    }

    private fun observeMetricsForCharts() {
        viewModelScope.launch {
            cpuMetrics.collect { metrics ->
                metrics?.history?.let { history ->
                    if (history.isNotEmpty()) {
                        val entries = history.mapIndexed { index, value -> entryOf(index, value) }
                        cpuChartProducer.setEntries(entries)
                    }
                }
            }
        }
        viewModelScope.launch {
            ramMetrics.collect { metrics ->
                metrics?.history?.let { history ->
                    if (history.isNotEmpty()) {
                        val entries = history.mapIndexed { index, value -> entryOf(index, value) }
                        ramChartProducer.setEntries(entries)
                    }
                }
            }
        }
    }
}
