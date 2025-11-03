
package com.example.inventariadosapp.admin.consulta.navigation

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.inventariadosapp.admin.consulta.ui.screens.AllReportsScreen
import com.example.inventariadosapp.admin.consulta.ui.screens.HomeScreenConsult
import com.example.inventariadosapp.admin.consulta.ui.screens.ReportsOverviewConsultScreen
import com.example.inventariadosapp.admin.consulta.viewmodel.ConsultViewModel
import com.example.inventariadosapp.admin.report.reportnavgraph.reportNavGraph
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes



fun NavGraphBuilder.consultNavGraph(navController: NavHostController) {


    navigation(
        startDestination = ConsultRoutes.HOME,
        route = ConsultRoutes.CONSULT_FLOW
    ) {

        // 1. RUTA: HOME (Pantalla principal con BottomBar)
        composable(ConsultRoutes.HOME) {
            val consultViewModel: ConsultViewModel = viewModel()
            HomeScreenConsult(navController = navController, viewModel = consultViewModel)
        }

        // 2. RUTA: ALL_REPORTS
        composable(ConsultRoutes.ALL_REPORTS) {
            val consultViewModel: ConsultViewModel = viewModel()
            AllReportsScreen(navController = navController, viewModel = consultViewModel)
        }

        // 3. RUTA: REPORTS_OVERVIEW
        composable(ConsultRoutes.REPORTS_OVERVIEW) {
            ReportsOverviewConsultScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 4. INTEGRACIÓN DEL GRAFO DE TU COMPAÑERO
        // Anidamos el grafo de Reportes dentro de este grafo.
        reportNavGraph(navController)
    }
}