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
// 📍 Definición de Rutas
// ====================================================================
object AssignRoutes {
    const val MENU = "assign_menu"
    const val CAMERA = "assign_camera"
    const val MANUAL = "assign_manual"
}

// ====================================================================
// 🚀 Gráfico de Navegación Principal (NavGraph)
// ====================================================================
@Composable
fun AssignNavGraph(navController: NavHostController) {
    val viewModel: TopografoAssignViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AssignRoutes.MENU
    ) {
        // 🏠 Pantalla principal del menú
        composable(AssignRoutes.MENU) {
            AssignMenuScreen(navController = navController)
        }

        // 📷 Pantalla de escaneo con cámara
        composable(AssignRoutes.CAMERA) {
            AssignCameraScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // ✍️ Pantalla de ingreso manual
        composable(AssignRoutes.MANUAL) {
            AssignManualScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
