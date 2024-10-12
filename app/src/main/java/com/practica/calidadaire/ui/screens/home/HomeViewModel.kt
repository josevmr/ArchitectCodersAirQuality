package com.practica.calidadaire.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica.calidadaire.R
import com.practica.calidadaire.data.DataRepository
import com.practica.calidadaire.data.model.AirCoordinates
import com.practica.calidadaire.data.model.HomeDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val repository = DataRepository()
    fun onUiReady(coordinates: AirCoordinates) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)

            val formattedCoordinates = formatCoordinates(coordinates)
            val cityDataList = repository.fetchLocationsData(formattedCoordinates)
            val nearestStationData = findNearestHomeData(coordinates, cityDataList)
            _state.value = UiState(
                loading = false,
                data = nearestStationData
            )
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val data: HomeDataModel = HomeDataModel(),
        val message: String = "",
        val isPermissionDeniedVisible: Boolean = false
    )

    fun onPermissionDenied() {
        _state.update { it.copy(
            message = ("Necesitas dar permiso en ajustes"),
            isPermissionDeniedVisible = true
        ) }
    }
    fun onMessageShown(){
        _state.update { it.copy(message = ("")) }
    }
    fun onSettingsButtonClicked(){
        _state.update { it.copy(isPermissionDeniedVisible = false)}
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c // Distance in kilometers
    }

    private fun findNearestHomeData(userCoordinates: AirCoordinates, homeDataList: List<HomeDataModel>): HomeDataModel {
        return homeDataList.minByOrNull { homeData ->
            haversine(
                userCoordinates.latitude,
                userCoordinates.longitude,
                homeData.coordinates.latitude,
                homeData.coordinates.longitude
            )
        } ?: HomeDataModel()
    }

    private fun formatCoordinates(coordinate: AirCoordinates): String {
        val latitude = Math.round(coordinate.latitude * 10000.0) / 10000.0
        val longitude = Math.round(coordinate.longitude * 10000.0) / 10000.0

        return "$latitude,$longitude"
    }

    fun getDescriptions() = mapOf(
        Pair(R.string.pm25_title, R.string.pm25_description),
        Pair(R.string.pm10_title, R.string.pm10_description),
        Pair(R.string.ozone_title, R.string.ozone_description),
        Pair(R.string.no_title, R.string.no_description),
        Pair(R.string.no2_title, R.string.no2_description),
        Pair(R.string.so2_title, R.string.so2_description),
        Pair(R.string.co_title, R.string.co_description),
        Pair(R.string.bc_title, R.string.bc_description)
    )

    fun parseDate(date: String): String {
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = OffsetDateTime.parse(date, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    fun calculateAirQualityIndex(): Double {
        val index = _state.value.data.parameters.map { parameter ->
            when (parameter.parameter) {
                "pm25" -> calculatePM25(parameter.lastValue)
                "pm10" -> calculatePM10(parameter.lastValue)
                "o3" -> calculateOzone(parameter.lastValue)
                "no2" -> calculateNO2(parameter.lastValue)
                "so2" -> calculateSO2(parameter.lastValue)
                "co" -> calculateCO(parameter.lastValue)
                else -> 0.0
            }
        }

        return index.average()
    }

    private fun calculatePM25(value: Double): Double {
        return when {
            value <= 12 -> 0.0
            value <= 35.4 -> 50.0
            value <= 55.4 -> 100.0
            value <= 150.4 -> 150.0
            value <= 250.4 -> 200.0
            value <= 350.4 -> 300.0
            value <= 500.4 -> 400.0
            else -> 500.0
        }
    }
    private fun calculatePM10(value: Double): Double {
        return when {
            value <= 54 -> 0.0
            value <= 154 -> 50.0
            value <= 254 -> 100.0
            value <= 354 -> 150.0
            value <= 424 -> 200.0
            value <= 604 -> 300.0
            else -> 500.0
        }
    }
    private fun calculateOzone(value: Double): Double {
        return when {
            value <= 54 -> 0.0
            value <= 70 -> 50.0
            value <= 85 -> 100.0
            value <= 105 -> 150.0
            value <= 200 -> 200.0
            value <= 400 -> 300.0
            else -> 500.0
        }
    }
    private fun calculateNO2(value: Double): Double {
        return when {
            value <= 53 -> 0.0
            value <= 100 -> 50.0
            value <= 360 -> 100.0
            value <= 649 -> 150.0
            value <= 1249 -> 200.0
            else -> 500.0
        }
    }
    private fun calculateSO2(value: Double): Double {
        return when {
            value <= 35 -> 0.0
            value <= 75 -> 50.0
            value <= 185 -> 100.0
            value <= 304 -> 150.0
            value <= 604 -> 200.0
            else -> 500.0
        }
    }
    private fun calculateCO(value: Double): Double {
        return when {
            value <= 4.4 -> 0.0
            value <= 9.4 -> 50.0
            value <= 12.4 -> 100.0
            value <= 15.4 -> 150.0
            value <= 30.4 -> 200.0
            else -> 500.0
        }
    }

    fun getParameterTitle(parameter: String, context: Context): String {
        return when (parameter) {
            "um003" -> context.getString(R.string.um003_title_with_unit)
            "pm1" -> context.getString(R.string.pm1_title_with_unit)
            "pm10" -> context.getString(R.string.pm10_title_with_unit)
            "relativehumidity" -> context.getString(R.string.relative_humidity_title_with_unit)
            "temperature" -> context.getString(R.string.temperature_with_unit)
            "pm25" -> context.getString(R.string.pm25_title_with_unit)
            "no2" -> context.getString(R.string.no2_title_with_unit)
            "nox" -> context.getString(R.string.nox_title_with_unit)
            "no" -> context.getString(R.string.no_title_with_unit)
            "o3" -> context.getString(R.string.o3_title_with_unit)
            "co" -> context.getString(R.string.co_title_with_unit)
            "so2" -> context.getString(R.string.so2_title_with_unit)
            else -> parameter
        }
    }


}