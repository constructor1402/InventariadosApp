package com.example.inventariadosapp.screens.admin


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.domain.model.Obra
import com.example.inventariadosapp.screens.admin.gestion.ObrasAdminScreen
import com.example.inventariadosapp.ui.screens.admin.InformeCompObraScreen
import com.example.inventariadosapp.ui.screens.admin.gestion.equipos.EquiposAdminScreen
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UsuariosAdminScreen
import com.example.inventariadosapp.ui.screens.admin.informes.InformeEquiposScreen
import com.example.inventariadosapp.ui.screens.admin.informes.InformeObrasScreen
import com.example.inventariadosapp.ui.screens.admin.informes.InformeUsuariosScreen
import com.example.inventariadosapp.ui.screens.admin.informes.InformesAdminScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument


@Composable
fun AdminNavigation(mainNavController: NavController, userCorreo: String) {
    val adminNavController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "inicio_admin/$userCorreo"
    ) {

        composable("inicio_admin/{userCorreo}") { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("userCorreo") ?: ""
            InicioAdminScreen(
                adminNavController = adminNavController,
                mainNavController = mainNavController,
                userCorreo = correo
            )
        }

        composable("gestion_admin") { ObrasAdminScreen(adminNavController) }
        composable("obras_admin") { ObrasAdminScreen(adminNavController) }
        composable("equipos_admin") { EquiposAdminScreen(adminNavController) }
        composable("usuarios_admin") { UsuariosAdminScreen(adminNavController) }

// Informes (ajustadas para coincidir con las rutas de los botones)
        composable(
            route = "informes_admin/{userCorreo}",
            arguments = listOf(navArgument("userCorreo") { type = NavType.StringType })
        ) { backStackEntry ->
            val userCorreo = backStackEntry.arguments?.getString("userCorreo") ?: ""
            InformesAdminScreen(navController = adminNavController, userCorreo)
        }

        composable(
            route = "informe_equipos/{userCorreo}",
            arguments = listOf(navArgument("userCorreo") { type = NavType.StringType })
        ) { backStackEntry ->
            val userCorreo = backStackEntry.arguments?.getString("userCorreo") ?: ""
            InformeEquiposScreen(navController = adminNavController, userCorreo)
        }

        composable(
            route = "informe_obras/{userCorreo}",
            arguments = listOf(navArgument("userCorreo") { type = NavType.StringType })
        ) { backStackEntry ->
            val userCorreo = backStackEntry.arguments?.getString("userCorreo") ?: ""
            InformeObrasScreen(navController = adminNavController, userCorreo)
        }

        composable(
            route = "informe_usuarios/{userCorreo}",
            arguments = listOf(navArgument("userCorreo") { type = NavType.StringType })
        ) { backStackEntry ->
            val userCorreo = backStackEntry.arguments?.getString("userCorreo") ?: ""
            InformeUsuariosScreen(navController = adminNavController, userCorreo)
        }



        composable("informeCompObraScreen/{userCorreo}") { backStackEntry ->
            val obrasFiltradas = adminNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Obra>>("obrasFiltradas")
                ?: emptyList()
            val correo = backStackEntry.arguments?.getString("userCorreo") ?: ""

            InformeCompObraScreen(
                adminNavController = adminNavController,
                obrasFiltradas = obrasFiltradas,
                viewModel = viewModel(),
                userCorreo = correo
            )
        }
    }
}
