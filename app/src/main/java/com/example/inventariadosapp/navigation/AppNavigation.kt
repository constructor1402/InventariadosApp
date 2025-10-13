package com.example.inventariadosapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.LoginScreen
import com.example.inventariadosapp.PanelAdminScreen
import com.example.inventariadosapp.PanelConsultaScreen
import com.example.inventariadosapp.PanelTopografoScreen
import com.example.inventariadosapp.RegisterScreen
import com.example.inventariadosapp.ResetPasswordScreen
import com.example.inventariadosapp.ui.screens.WelcomeScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "bienvenida") {
        composable(route = "bienvenida") { WelcomeScreen(navController) }
        composable(route = "login") { LoginScreen(navController) }
        composable(route = "registro") { RegisterScreen(navController) }
        composable(route = "reset") { ResetPasswordScreen(navController) }
        composable(route = "panel_admin") { PanelAdminScreen(navController) }
        composable(route = "panel_topografo") { PanelTopografoScreen(navController) }
        composable(route = "panel_consulta") { PanelConsultaScreen(navController) }
    }
}


