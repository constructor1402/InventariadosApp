package com.example.inventariadosapp.screens.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AdminNavigation() {
    // 🔹 Este NavController manejará solo las pantallas internas del panel admin
    val adminNavController: NavHostController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "inicio_admin"
    ) {
        // 🏠 Pantalla principal del admin (con la barra inferior)
        composable("inicio_admin") {
            InicioAdminScreen(adminNavController)
        }

        // 🧾 Gestión de usuarios
        composable("gestion_admin") {
            GestionAdminScreen(adminNavController)
        }

        // 📊 Informes
        composable("informes_admin") {
            InformesAdminScreen(adminNavController)
        }
    }
}


