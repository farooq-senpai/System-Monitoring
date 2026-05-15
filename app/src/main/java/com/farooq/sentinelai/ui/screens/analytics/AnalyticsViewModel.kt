package com.farooq.sentinelai.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farooq.sentinelai.data.repository.TelemetryRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val telemetryRepository: TelemetryRepository
) : ViewModel() {

    val cpuChartProducer = ChartEntryModelProducer()
    val ramChartProducer = ChartEntryModelProducer()
    val networkChartProducer = ChartEntryModelProducer()

    init {
        viewModelScope.launch {
            telemetryRepository.cpuMetrics.collect { metrics ->
                cpuChartProducer.setEntries(metrics.history.mapIndexed { i, v -> entryOf(i, v) })
            }
        }
        viewModelScope.launch {
            telemetryRepository.ramMetrics.collect { metrics ->
                ramChartProducer.setEntries(metrics.history.mapIndexed { i, v -> entryOf(i, v) })
            }
        }
        viewModelScope.launch {
            telemetryRepository.networkMetrics.collect { metrics ->
                networkChartProducer.setEntries(metrics.downloadHistory.mapIndexed { i, v -> entryOf(i, v) })
            }
        }
    }
}
