package com.practica.calidadaire.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoricRequestDataModel(
    val meta: HistoricMeta,
    val results: List<HistoricResult>
)
@Serializable
data class HistoricResult(
    val city: String? = "",
    val coordinates: HistoricCoordinates,
    val country: String,
    val date: HistoricDate,
    val entity: String,
    val isAnalysis: Boolean? = true,
    val isMobile: Boolean,
    val location: String,
    val locationId: Int,
    val parameter: String,
    val sensorType: String,
    val unit: String,
    val value: Double
)
@Serializable
data class HistoricCoordinates(
    val latitude: Double,
    val longitude: Double
)
@Serializable
data class HistoricDate(
    val local: String,
    val utc: String
)
@Serializable
data class HistoricMeta(
    val found: String,
    val license: String,
    val limit: Int,
    val name: String,
    val page: Int,
    val website: String
)