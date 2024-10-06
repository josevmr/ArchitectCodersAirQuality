package com.practica.calidadaire.data

import com.practica.calidadaire.data.model.AirCoordinates
import com.practica.calidadaire.data.model.AirParameter
import com.practica.calidadaire.data.model.HistoricDataModel
import com.practica.calidadaire.data.model.HistoricResult
import com.practica.calidadaire.data.model.HomeDataModel
import com.practica.calidadaire.data.model.StationCoordinates
import com.practica.calidadaire.data.model.StationParameter
import com.practica.calidadaire.data.model.StationResult

class DataRepository {

    suspend fun fetchLocationsData(coordinates: String): List<HomeDataModel> =
        DataClient
            .instance
            .fetchLocationsData(coordinates, 5000)
            .results
            .map { it.toDomainModel() }

    suspend fun fetchHistoricData(dateFrom: String, dateTo: String, location: String): List<HistoricDataModel> =
        DataClient
            .instance
            .fetchHistoricData(dateFrom, dateTo, location)
            .results
            .map { it.toDomainModel() }
}

private fun StationResult.toDomainModel(): HomeDataModel =
    HomeDataModel(
        id = id,
        name = name,
        parameters = parameters.map { it.toDomainModel() },
        coordinates = coordinates.toDomainModel() ,
        lastUpdated = lastUpdated
    )

private fun StationParameter.toDomainModel(): AirParameter =
    AirParameter(
        parameter = parameter,
        average = average,
        lastValue = lastValue,
        lastUpdated = lastUpdated
        )

private fun StationCoordinates.toDomainModel(): AirCoordinates =
    AirCoordinates (
        latitude = latitude,
        longitude = longitude
    )

private fun HistoricResult.toDomainModel(): HistoricDataModel =
    HistoricDataModel(
        date = date.local,
        parameter = parameter,
        unit = unit,
        value = value
    )