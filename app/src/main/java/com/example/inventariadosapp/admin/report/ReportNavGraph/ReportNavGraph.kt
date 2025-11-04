

package com.example.inventariadosapp.admin.report.reportnavgraph

import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inventariadosapp.admin.report.components.models.ReportSearchScreen
import com.example.inventariadosapp.admin.report.components.models.ReportResultsScreen
import com.example.inventariadosapp.admin.report.components.models.ReportSuccessScreen




fun NavGraphBuilder.reportNavGraph(navController: NavHostController) {


    navigation(
        startDestination = ReportRoutes.SEARCH,
        route = "report_flow"
    ) {

        composable(ReportRoutes.SEARCH) {
            ReportSearchScreen(navController = navController)
        }

        composable(
            route = "${ReportRoutes.RESULTS}/{codigoBuscado}",
            arguments = listOf(navArgument("codigoBuscado") { type = NavType.StringType })
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("codigoBuscado") ?: ""
            val codigoBuscado = java.net.URLDecoder.decode(raw, "UTF-8")
            ReportResultsScreen(
                navController = navController,
                codigoBuscado = codigoBuscado
            )
        }

        composable(
            route = "${ReportRoutes.SUCCESS}/{codigoBuscado}",
            arguments = listOf(navArgument("codigoBuscado") { type = NavType.StringType })
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("codigoBuscado") ?: ""
            val codigoBuscado = java.net.URLDecoder.decode(raw, "UTF-8")
            ReportSuccessScreen(
                navController = navController,
                codigoBuscado = codigoBuscado
            )
        }
    }
}