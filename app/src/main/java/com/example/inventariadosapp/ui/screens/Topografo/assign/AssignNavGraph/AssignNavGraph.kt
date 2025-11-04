package com.example.inventariadosapp.ui.screens.Topografo.assign.AssignNavGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.AssignMenuScreen
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.AssignCameraScreen
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.AssignManualScreen
import com.example.inventariadosapp.ui.screens.Topografo.assign.models.TopografoAssignViewModel


object AssignRoutes {
    const val MENU = "assign_menu"
    const val CAMERA = "assign_camera"
    const val MANUAL = "assign_manual"
}

@Composable
fun AssignNavGraph(navController: NavHostController) {
    val viewModel: TopografoAssignViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AssignRoutes.MENU
    ) {
        composable(AssignRoutes.MENU) {
            AssignMenuScreen(navController, viewModel)
        }
        composable(AssignRoutes.CAMERA) {
            AssignCameraScreen(navController, viewModel)
        }
        composable(AssignRoutes.MANUAL) {
            AssignManualScreen(navController, viewModel)
        }
    }
}


