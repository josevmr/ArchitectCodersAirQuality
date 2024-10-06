package com.practica.calidadaire.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StationDataRequestModel(
    val meta: StationMeta,
    val results: List<StationResult>
)

@Serializable
data class StationResult(
    val bounds: List<Double>,
    val city: String? = "",
    val coordinates: StationCoordinates,
    val country: String,
    val entity: String? = "",
    val firstUpdated: String,
    val id: Int,
    val isAnalysis: Boolean? = false,
    val isMobile: Boolean,
    val lastUpdated: String,
    val manufacturers: List<StationManufacturer>? = null,
    val measurements: Int,
    val name: String,
    val parameters: List<StationParameter>,
    val sensorType: String? = "",
    val sources: List<StationSources>? = null
)

@Serializable
data class StationParameter(
    val average: Double,
    val count: Int,
    val displayName: String,
    val firstUpdated: String,
    val id: Int,
    val lastUpdated: String,
    val lastValue: Double,
    val manufacturers: List<StationManufactures>? = null,
    val parameter: String,
    val parameterId: Int,
    val unit: String
)

@Serializable
data class StationMeta(
    val found: Int,
    val license: String,
    val limit: Int,
    val name: String,
    val page: Int,
    val website: String
)

@Serializable
data class StationManufacturer(
    val manufacturerName: String? = "",
    val modelName: String? = ""
)

@Serializable
data class StationCoordinates(
    val latitude: Double,
    val longitude: Double
)
@Serializable
data class StationManufactures(
    val modelName: String,
    val manufacturerName: String
)
@Serializable
data class StationSources(
    val url: String? = "",
    val name: String? = "",
    val id: String? = "",
    val readme: String? = "",
    val organization: String? = "",
    val lifecycle_stage: String? = ""
)