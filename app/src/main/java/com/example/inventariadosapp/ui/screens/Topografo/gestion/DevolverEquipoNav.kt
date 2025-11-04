package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.inventariadosapp.ui.screens.Topografo.gestion.DevolverEquipoScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.EscanearSerialScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.IngresoManualScreen

@Composable
fun DevolverEquipoNav(parentNavController: NavHostController) {
    NavHost(
        navController = parentNavController,
        startDestination = "devolver_main/{serialArg}"
    ) {
        // Pantalla principal de devolución
        composable(route = "devolver_main/{serialArg}") { backStackEntry ->
            val serialArg = backStackEntry.arguments?.getString("serialArg") ?: ""

            DevolverEquipoScreen(
                serialArg = serialArg,
                navController = parentNavController,
                onScanClick = { parentNavController.navigate("devolver_scan") },
                onManualClick = { parentNavController.navigate("devolver_manual/$serialArg") },
                onConfirmarDevolucion = {
                    parentNavController.navigate("gestion_topografo") {
                        popUpTo("gestion_topografo") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de escanear código
        composable(route = "devolver_scan") {
            EscanearSerialScreen(navController = parentNavController)
        }

        // Pantalla de ingreso manual
        composable(
            route = "devolver_manual/{serial}",
            arguments = listOf(
                navArgument("serial") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val serial = backStackEntry.arguments?.getString("serial") ?: ""
            IngresoManualScreen(
                navController = parentNavController,
                serialArg = serial
            )
        }
    }
}
