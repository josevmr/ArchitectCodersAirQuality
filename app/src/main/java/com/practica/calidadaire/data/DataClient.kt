package com.practica.calidadaire.data

import com.practica.calidadaire.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

object DataClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { apiKeyAsHeader(it) }
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance = Retrofit.Builder()
        .baseUrl("https://api.openaq.org/v2/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create<DataService>()
}

private fun apiKeyAsHeader(chain: Interceptor.Chain) = chain.proceed(
    chain.request()
        .newBuilder()
        .addHeader("X-API-Key", BuildConfig.AQ_API_KEY)
        .build()
)