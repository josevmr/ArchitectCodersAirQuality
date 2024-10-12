package com.practica.calidadaire.ui.screens.historic

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.calidadaire.data.DataRepository
import com.practica.calidadaire.data.model.HistoricDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoricScreenViewModel: ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val repository = DataRepository()

    fun onUiReady(location: String) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)

            val today = LocalDate.now()
            val before7days = today.minusDays(7)

            _state.value = UiState(
                loading = false,
                data = repository.fetchHistoricData(before7days.toString(), today.toString(), location)
            )
        }
    }

    inner class UiState(
        val loading: Boolean = true,
        val data: List<HistoricDataModel> = emptyList()
    )

    fun calculateAverageByParameter(list: List<HistoricDataModel>): Map<String, Map<String, Double>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val groupedByDayAndParameter = list.groupBy { parameter ->
            Pair(LocalDate.parse(parameter.date.substring(0, 10), formatter), parameter.parameter)
        }

        return groupedByDayAndParameter.mapValues { entry ->
            val dailyMeasurements = entry.value
            val total = dailyMeasurements.sumOf { it.value }
            val count = dailyMeasurements.size
            total / count
        }.entries.groupBy(
            { it.key.first.toString() },
            { it.key.second to it.value }
        ).mapValues { it.value.toMap() }
    }
}