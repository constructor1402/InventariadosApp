package com.example.inventariadosapp.admin.topografo.assign.assignnavgraph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventariadosapp.admin.topografo.assign.components.models.TopografoAssignViewModel
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignCameraScreen
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignManualScreen
import com.example.inventariadosapp.admin.topografo.assign.components.models.AssignMenuScreen

// ====================================================================
// Rutas de Navegación
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
    val viewModel: TopografoAssignViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AssignRoutes.MENU
    ) {
        // Pantalla principal del menú
        composable(AssignRoutes.MENU) {
            AssignMenuScreen(
                onNavigateToCamera = { navController.navigate(AssignRoutes.CAMERA) },
                onNavigateToManual = { navController.navigate(AssignRoutes.MANUAL) }
            )
        }

        // Pantalla de escaneo con cámara (con botón de atrás)
        composable(AssignRoutes.CAMERA) {
            AssignCameraScreen(
                viewModel = viewModel,
                navController = navController // ✅ agregado
            )
        }

        // Pantalla de ingreso manual (con botón de atrás)
        composable(AssignRoutes.MANUAL) {
            AssignManualScreen(
                viewModel = viewModel,
                navController = navController // ✅ agregado
            )
        }
    }
}
