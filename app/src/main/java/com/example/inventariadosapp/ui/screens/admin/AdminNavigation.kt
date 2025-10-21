package com.example.inventariadosapp.screens.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.screens.admin.gestion.EquiposAdminScreen
import com.example.inventariadosapp.screens.admin.gestion.ObrasAdminScreen
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UsuariosAdminScreen


@Composable
fun AdminNavigation(mainNavController: NavController) {
    val adminNavController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "inicio_admin"
    ) {
        // Pantalla principal
        composable("inicio_admin") {
            InicioAdminScreen(
                adminNavController = adminNavController,
                mainNavController = mainNavController
            )
        }

        // Sección de gestión (grupo interno)
        composable("gestion_admin") { ObrasAdminScreen(adminNavController) }

        // Pantallas internas del grupo de gestión
        composable("obras_admin") { ObrasAdminScreen(adminNavController) }
        composable("equipos_admin") { EquiposAdminScreen(adminNavController) }
        composable("usuarios_admin") { UsuariosAdminScreen(adminNavController) }

        // Otros paneles
        composable("informes_admin") { InformesAdminScreen(adminNavController) }
    }
}
