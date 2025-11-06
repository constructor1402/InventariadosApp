package com.example.inventariadosapp.admin.consulta.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inventariadosapp.admin.consulta.ui.ReportsMenuScreen
import com.example.inventariadosapp.admin.consulta.ui.StorageReportsScreen
import com.example.inventariadosapp.admin.consulta.ui.UserReportsScreen
import com.example.inventariadosapp.admin.consulta.ui.UsersListScreen
import com.example.inventariadosapp.admin.consulta.ui.screens.HomeScreenConsult
import com.example.inventariadosapp.admin.consulta.viewmodel.ConsultViewModel
import com.example.inventariadosapp.admin.report.reportnavgraph.reportNavGraph

fun NavGraphBuilder.consultNavGraph(navController: NavHostController) {
    navigation(
        startDestination = ConsultRoutes.HOME,
        route = ConsultRoutes.CONSULT_FLOW
    ) {
        composable(ConsultRoutes.HOME) {
            val consultViewModel: ConsultViewModel = viewModel()
            HomeScreenConsult(navController = navController, viewModel = consultViewModel)
        }

// 🔹 Pantalla del menú intermedio de reportes
        composable("reportsMenu") {
            ReportsMenuScreen(navController = navController)
        }

// 🔹 Pantalla de visualización de reportes en Storage (con parámetro dinámico)
        composable(
            route = "storageReports/{folderPath}",
            arguments = listOf(navArgument("folderPath") { type = NavType.StringType })
        ) { backStackEntry ->
            val folderPath = backStackEntry.arguments?.getString("folderPath") ?: "informes"
            StorageReportsScreen(folderPath = folderPath)
        }

        // 🔹 Lista de usuarios
        composable("usersList") {
            UsersListScreen(navController = navController)
        }

// 🔹 Informes por usuario
        composable(
            "userReports/{correoUsuario}"
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correoUsuario") ?: ""
            UserReportsScreen(navController = navController, correoUsuario = correo)
        }


        reportNavGraph(navController)
    }
}

