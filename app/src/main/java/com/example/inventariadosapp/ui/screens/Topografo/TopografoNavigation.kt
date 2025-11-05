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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.ui.screens.Topografo.assign.AssignNavGraph.AssignRoutes
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.AssignCameraScreen
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.AssignManualScreen
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.TopografoAssignViewModel
import com.example.inventariadosapp.ui.screens.Topografo.informes.InformesTopografoScreen


@Composable
fun TopografoNavigation(parentNavController: NavHostController) {
    val navController = rememberNavController()


    //INICIALIZA EL VIEWMODEL PARA ASIGNAR
    val assignViewModel: TopografoAssignViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "inicio_topografo"
    ) {
        // 🏠 Pantalla de inicio
        composable(route = "inicio_topografo") {
            InicioTopografoScreen(
                navController = navController,
                parentNavController = parentNavController
            )
        }


        // ⚙️ Pantalla de gestión (Asignar / Devolver)
        composable(route = "gestion_topografo") {
            //  👇  --- ARREGLO 2 (Error 'viewModel') ---
            GestionTopografoScreen(navController, assignViewModel)
        }

        // --- RUTAS DE ASIGNACIÓN (usando assignViewModel) ---
        composable(route = AssignRoutes.CAMERA) {
            AssignCameraScreen(navController = navController, viewModel = assignViewModel)
        }
        composable(route = AssignRoutes.MANUAL) {
            AssignManualScreen(navController = navController, viewModel = assignViewModel)
        }

        // --- RUTAS DE DEVOLUCIÓN (Como en tu archivo original) ---

        // 📷 Escanear serial del equipo (Para Devolver)
        composable(route = "escanear_serial") {
            //  👇  --- ARREGLO 3 (Error 'Too many arguments') ---
            // Esta ruta, según tu archivo original, no usa lambda
            EscanearSerialScreen(navController)
        }

        // ✍️ Ingreso manual del equipo (Para Devolver)
        composable(
            route = "ingreso_manual/{serialArg}"
        ) { backStackEntry ->
            val serialArg = backStackEntry.arguments?.getString("serialArg") ?: ""
            IngresoManualScreen(serialArg = serialArg, navController = navController)
        }

        // 🔁 Devolver equipo (flujo completo)
        composable(route = "devolver_equipo/{serialArg}") { backStackEntry ->
            val serialArg = backStackEntry.arguments?.getString("serialArg") ?: ""

            DevolverEquipoScreen(
                serialArg = serialArg,
                navController = navController,
                onScanClick = { navController.navigate("escanear_serial") },
                onManualClick = { navController.navigate("ingreso_manual/$serialArg") },
                onConfirmarDevolucion = {
                    navController.navigate("gestion_topografo") {
                        popUpTo("gestion_topografo") { inclusive = true }
                    }
                }
            )
        }
        // 📊 Pantalla de Informes del Topógrafo
        composable(route = "informes_topografo") {
            InformesTopografoScreen(navController)
        }

    }
}