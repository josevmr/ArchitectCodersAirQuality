package com.practica.calidadaire.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practica.calidadaire.ui.screens.historic.HistoricScreen
import com.practica.calidadaire.ui.screens.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(onClick = {location ->
                navController.navigate("historic/$location")
            })
        }
        composable("historic/{location}") { backStackEntry ->
            val location = backStackEntry.arguments?.getString("location")
            HistoricScreen(
                location = location,
                onBack = { navController.popBackStack() }
            )
        }
    }
}