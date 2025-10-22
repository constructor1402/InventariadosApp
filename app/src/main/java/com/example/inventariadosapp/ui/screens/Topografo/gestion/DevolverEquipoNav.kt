package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inventariadosapp.ui.screens.Topografo.gestion.DevolverEquipoScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.EscanearSerialScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.IngresoManualScreen


@Composable
fun DevolverEquipoNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "devolver_main"
    ) {
        composable("devolver_main") {
            DevolverEquipoScreen(
                navController = navController,
                onScanClick = { navController.navigate("devolver_scan") },
                onManualClick = { navController.navigate("devolver_manual") }
            )
        }



        composable("devolver_scan") {
            EscanearSerialScreen(navController)
        }

        composable(
            route = "devolver_manual?serial={serial}",
            arguments = listOf(
                navArgument("serial") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val serial = backStackEntry.arguments?.getString("serial")
            IngresoManualScreen(
                navController = navController,
                serialArg = serial
            )
        }
        }
    }

