package com.example.inventariadosapp.admin.report.reportnavgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inventariadosapp.admin.report.components.models.ReportSearchScreen
import com.example.inventariadosapp.admin.report.components.models.ReportResultsScreen
import com.example.inventariadosapp.admin.report.components.models.ReportSuccessScreen
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes

/**
 * NavGraph para reportes. La ruta RESULTS acepta argumento:
 * "report_results/{codigoBuscado}"
 * Y SUCCESS acepta también el mismo argumento:
 * "report_success/{codigoBuscado}"
 */
@Composable
fun ReportNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ReportRoutes.SEARCH
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
