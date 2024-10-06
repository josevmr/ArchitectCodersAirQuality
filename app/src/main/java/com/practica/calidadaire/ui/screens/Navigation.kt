package com.practica.calidadaire.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practica.calidadaire.ui.screens.historic.HistoricScreen
import com.practica.calidadaire.ui.screens.home.HomeScreen

sealed class NavScreen(val screen: String) {
    data object Home: NavScreen("home")
    data object Detail: NavScreen("historic/{${NavArgs.Location.key}}") {
        fun onCreateRoute(location: String) = "historic/${location}"
    }
}

enum class NavArgs(val key: String){
    Location("location")
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.screen
    ) {
        composable(NavScreen.Home.screen) {
            HomeScreen(onClick = {location ->
                navController.navigate(NavScreen.Detail.onCreateRoute(location))
            })
        }
        composable(NavScreen.Detail.screen) { backStackEntry ->
            val location = backStackEntry.arguments?.getString(NavArgs.Location.key)
            HistoricScreen(
                location = location,
                onBack = { navController.popBackStack() }
            )
        }
    }
}