package com.practica.calidadaire.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.practica.calidadaire.data.model.AirCoordinates
import com.practica.calidadaire.ui.utils.extensions.PermissionRequestEffect
import com.practica.calidadaire.ui.utils.extensions.getCoordinates
import kotlinx.coroutines.launch

class HomeState(
    val snackbarHostState: SnackbarHostState
) {
    private lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun AskCoordinatesEffect(onCoordinates: (AirCoordinates?) -> Unit) {
        context = LocalContext.current.applicationContext
        val coroutineScope = rememberCoroutineScope()
        var coordinates: AirCoordinates?

        PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
            if (granted) {
                coroutineScope.launch {
                    coordinates = context.getCoordinates()
                    onCoordinates(coordinates)
                }
            } else {
                onCoordinates(null)
            }
        }
    }

    @Composable
    fun ShowMessageEffect(message: String?, onMessageShown: () -> Unit) {
        LaunchedEffect(message) {
            message?.let {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(it)
                onMessageShown()
            }
        }
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

@Composable
fun rememberHomeState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): HomeState {
    return remember(snackbarHostState) { HomeState(snackbarHostState) }
}