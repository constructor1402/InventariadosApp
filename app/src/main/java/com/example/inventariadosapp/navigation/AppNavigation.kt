package com.example.inventariadosapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.screens.*
import com.example.inventariadosapp.LoginScreen
import com.example.inventariadosapp.PanelAdminScreen
import com.example.inventariadosapp.PanelConsultaScreen
import com.example.inventariadosapp.PanelTopografoScreen
import com.example.inventariadosapp.ui.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "bienvenida"
    ) {
        // 🏠 Pantalla principal o inicial
        composable("bienvenida") { WelcomeScreen(navController) }

        // 🔐 Login
        composable("login") { LoginScreen(navController) }

        // 🧾 Registro
        composable("registro") { RegisterScreen(navController) }

        // 🔄 Recuperar contraseña (ingreso del número y código)
        composable("recuperar_contrasena") { RecuperarContrasenaScreen(navController) }

        // 🔑 Restablecer contraseña (recibe el teléfono)
        composable("restablecer_contrasena/{telefono}") { backStackEntry ->
            val telefono = backStackEntry.arguments?.getString("telefono")
            RestablecerContrasenaScreen(navController, telefono)
        }

        // 👇 Ejemplos de paneles (ajusta según tus pantallas reales)
        composable("panel_admin") { PanelAdminScreen(navController) }
        composable("panel_topografo") { PanelTopografoScreen(navController) }
        composable("panel_consulta") { PanelConsultaScreen(navController) }
    }
}


