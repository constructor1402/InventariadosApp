
package com.example.inventariadosapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.inventariadosapp.admin.consulta.navigation.ConsultRoutes
import com.example.inventariadosapp.admin.consulta.navigation.consultNavGraph
import com.example.inventariadosapp.screens.*
import com.example.inventariadosapp.ui.screens.login.WelcomeScreen
import com.example.inventariadosapp.ui.screens.Topografo.TopografoNavigation
import com.example.inventariadosapp.screens.admin.AdminNavigation
import com.example.inventariadosapp.LoginScreen
import com.example.inventariadosapp.PanelConsultaScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    // 1. Ruta de inicio de la aplicación
    val START_DESTINATION = "bienvenida"

    NavHost(
        navController = navController,
        startDestination = START_DESTINATION
    ) {
        // --- RUTAS DE LOGIN Y BIENVENIDA (Simples) ---
        composable("bienvenida") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegisterScreen(navController) }
        composable("recuperar_contrasena") { RecuperarContrasenaScreen(navController) }


        composable("restablecer_contrasena/{telefono}") { backStackEntry ->
            val telefono = backStackEntry.arguments?.getString("telefono")
            RestablecerContrasenaScreen(navController, telefono)
        }




        consultNavGraph(navController)

        // 🟡 FLUJOS DE ADMIN Y TOPÓGRAFO
        composable("panel_admin") { AdminNavigation(navController) }
        composable("panel_topografo") { TopografoNavigation(navController) }


        composable("panel_consulta") { PanelConsultaScreen(navController) }
    }
}