package com.practica.calidadaire

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practica.calidadaire.ui.screens.Navigation
import com.practica.calidadaire.ui.screens.historic.HistoricScreen
import com.practica.calidadaire.ui.screens.home.HomeScreen
import com.practica.calidadaire.ui.theme.CalidadAireTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalidadAireTheme {
                Navigation()
            }
        }
    }
}
