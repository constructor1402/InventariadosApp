package com.example.inventariadosapp.ui.screens.topografo

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.screens.Topografo.InicioTopografoScreen

@Composable
fun TopografoNavigation(parentNavController: NavController) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio_topografo"
    ) {
        // 🏠 Inicio
        composable("inicio_topografo") {
            InicioTopografoScreen(navController)
        }

        // 🧰 Gestión de equipos (puedes crearla después)
        composable("gestion_topografo") {
            // GestionTopografoScreen(navController)
        }

        // 📊 Informes
        composable("informes_topografo") {
            // InformesTopografoScreen(navController)
        }
    }
}
