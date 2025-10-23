package com.example.inventariadosapp.ui.screens.Topografo

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.screens.Topografo.gestion.DevolverEquipoScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.IngresoManualScreen
import com.example.inventariadosapp.ui.screens.Topografo.gestion.EscanearSerialScreen

@Composable
fun TopografoNavigation(parentNavController: NavController) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio_topografo"
    ) {
        // 🏠 Pantalla principal del topógrafo
        composable(route = "inicio_topografo") {
            InicioTopografoScreen(navController)
        }

        // ⚙️ Pantalla de gestión (Asignar / Devolver)
        composable(route = "gestion_topografo") {
            GestionTopografoScreen(navController)
        }

        // 📷 Escanear serial del equipo
        composable(route = "escanear_serial") {
            EscanearSerialScreen(navController)
        }

        // ✍️ Ingreso manual del equipo
        composable(
            route = "ingreso_manual/{serialArg}"
        ) { backStackEntry ->
            val serialArg = backStackEntry.arguments?.getString("serialArg") ?: ""
            IngresoManualScreen(serialArg = serialArg, navController = navController)
        }

        // 🔁 Devolver equipo (flujo completo con escanear e ingreso manual)
        composable(route = "devolver_equipo/{serialArg}") { backStackEntry ->
            val serialArg = backStackEntry.arguments?.getString("serialArg") ?: ""

            DevolverEquipoScreen(
                serialArg = serialArg,
                navController = navController,
                onScanClick = { navController.navigate("escanear_serial") },
                onManualClick = { navController.navigate("ingreso_manual/$serialArg") },
                onConfirmarDevolucion = {
                    // 💡 Acción tras confirmar la devolución
                    navController.navigate("gestion_topografo") {
                        popUpTo("gestion_topografo") { inclusive = true }
                    }
                }
            )
        }


    }
}
