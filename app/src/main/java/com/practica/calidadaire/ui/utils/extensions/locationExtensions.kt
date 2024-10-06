package com.practica.calidadaire.ui.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.practica.calidadaire.data.model.AirCoordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

const val DEFAULT_CITY = "Madrid"


/**
 * Método para obtener la ciudad a través del servicio de Google
 * FusedLocation obtiene las últimas localizaciones guardadas del usuario
 * Geocoder para obtener la información a través de la localización
 * */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun Context.getCoordinates(): AirCoordinates {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    val location = fusedLocationClient.lastLocation()

    return if (location != null) {
       AirCoordinates(
           latitude = location.latitude,
           longitude = location.longitude
       )
    } else AirCoordinates()
}

fun roundToFourDecimals(value: Double): Double {
    return Math.round(value * 10000.0) / 10000.0
}

/**
 * Método para obtener la ciudad a través del servicio de Google
 * FusedLocation obtiene las últimas localizaciones guardadas del usuario
 * Geocoder para obtener la información a través de la localización
 * */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun Context.getCity(): String {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    val location = fusedLocationClient.lastLocation()

    val geocoder = Geocoder(this)
    val addresses = location?.let {
        geocoder.getFromLocationCompat(it.latitude, it.longitude, 1)
    }

    val region = addresses?.first()?.subAdminArea
    return region ?: DEFAULT_CITY
}

@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.lastLocation(): Location? {
    return suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location)
        }.addOnFailureListener {
            continuation.resume( null)
        }
    }
}
/**
 * Método para obtener el listado de localizaciones según la versión que tengamos, ya que la
 * versión antigua está deprecada al no tener Callback y poder ser invocada en el hilo principal
 * */
@Suppress("DEPRECATION")
suspend fun Geocoder.getFromLocationCompat(
    @FloatRange(from = -90.0, to = 90.0) latitude: Double,
    @FloatRange(from = -180.0, to = 180.0) longitude: Double,
    maxResults: Int
): List<Address> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    suspendCancellableCoroutine { continuation ->
        getFromLocation(latitude, longitude, maxResults) {
            continuation.resume(it)
        }
    }} else {
    withContext(Dispatchers.IO) {
        getFromLocation(latitude, longitude, maxResults) ?: emptyList()
    }
}
