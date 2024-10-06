package com.practica.calidadaire.data

import com.practica.calidadaire.data.model.HistoricRequestDataModel
import com.practica.calidadaire.data.model.StationDataRequestModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DataService {

    @GET("locations")
    suspend fun fetchLocationsData(
        @Query("coordinates")coordinates: String,
        @Query("radius")radius: Int
    ): StationDataRequestModel

    @GET("measurements")
    suspend fun fetchHistoricData(
        @Query("date_from")dateFrom: String,
        @Query("date_to")dateTo: String,
        @Query("location")location: String
    ): HistoricRequestDataModel

}