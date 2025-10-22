package com.example.inventariadosapp.admin.topografo.assign.AssignNavGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

// CRUCIAL: Importación del ViewModel (paquete 'assign' padre)
import com.example.inventariadosapp.admin.topografo.assign.components.models.TopografoAssignViewModel

// CRUCIAL: Importaciones de las Pantallas (paquete 'components.models')
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignCameraScreen
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignManualScreen
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignMenuScreen


// ====================================================================
// Rutas de Navegación (Rutas de pantalla)
// ====================================================================

object AssignRoutes {
    const val MENU = "assign_menu"
    const val CAMERA = "assign_camera"
    const val MANUAL = "assign_manual"
}


// ====================================================================
// NavGraph Principal
// ====================================================================

@Composable
fun AssignNavGraph(navController: NavHostController) {
    // Inicializa el ViewModel
    // La inyección de ViewModel debe hacerse aquí o en las pantallas, pero no ambos
    val viewModel: TopografoAssignViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AssignRoutes.MENU
    ) {

        // 1. AssignMenuScreen (Usa lambdas de navegación)
        composable(AssignRoutes.MENU) {
            AssignMenuScreen(
                // Aquí usamos lambdas para llamar a la navegación, ya que AssignMenuScreen.kt lo espera.
                onNavigateToCamera = { navController.navigate(AssignRoutes.CAMERA) },
                onNavigateToManual = { navController.navigate(AssignRoutes.MANUAL) }
            )
        }

        // 2. AssignCameraScreen (Llamada simple, el ViewModel se crea internamente)
        composable(AssignRoutes.CAMERA) {
            AssignCameraScreen()
        }

        // 3. AssignManualScreen (Llamada simple, el ViewModel se crea internamente)
        composable(AssignRoutes.MANUAL) {
            AssignManualScreen()
        }
    }
}