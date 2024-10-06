package com.practica.calidadaire.ui.utils.extensions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Función para almacenar el estado del permiso, de manera que no se vuelva a pedir el permiso
 * cada vez que se recomponga, esto es gracias a LaunchedEffect, este se recompondrá cada vez que
 * reciba el parámetro, pero al pasarle Unit, no se recompondrá
 * */
@Composable
fun PermissionRequestEffect(permission: String, onResult: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            onResult(it)
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }
}