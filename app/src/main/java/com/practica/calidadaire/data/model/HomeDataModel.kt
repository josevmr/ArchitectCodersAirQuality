package com.practica.calidadaire.data.model

import androidx.compose.ui.platform.LocalContext
import com.practica.calidadaire.R


data class HomeDataModel (
    val id: Int = 0,
    val name: String = "",
    val parameters: List<AirParameter> = emptyList(),
    val coordinates: AirCoordinates = AirCoordinates(),
    val lastUpdated: String = ""
)

data class AirParameter(
    val parameter: String,
    val average: Double,
    val lastValue: Double,
    val lastUpdated: String
)

data class AirCoordinates(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
