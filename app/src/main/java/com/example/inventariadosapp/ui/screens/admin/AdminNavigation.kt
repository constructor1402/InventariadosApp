package com.example.inventariadosapp.screens.admin

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.screens.admin.gestion.EquiposAdminScreen
import com.example.inventariadosapp.screens.admin.gestion.Obra
import com.example.inventariadosapp.screens.admin.gestion.ObrasAdminScreen
import com.example.inventariadosapp.screens.admin.gestion.UsuariosAdminScreen
import com.example.inventariadosapp.ui.screens.admin.InformeCompObraScreen
import com.example.inventariadosapp.ui.screens.admin.InformeCompScreen
import com.example.inventariadosapp.ui.screens.admin.InformeCompUsers
import com.example.inventariadosapp.ui.screens.admin.InformeObrasScreen
import com.example.inventariadosapp.ui.screens.admin.InformeUsuariosScreen
import com.example.inventariadosapp.ui.screens.admin.InformesEquiposScreen



@Composable
fun AdminNavigation(mainNavController: NavController) {
    val adminNavController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "inicio_admin"
    ) {
        // Pantalla principal
        composable("inicio_admin") {
            InicioAdminScreen(
                adminNavController = adminNavController,
                mainNavController = mainNavController
            )
        }

        // Sección de gestión (grupo interno)
        composable("gestion_admin") { ObrasAdminScreen(adminNavController) }

        // Pantallas internas del grupo de gestión
        composable("obras_admin") { ObrasAdminScreen(adminNavController) }
        composable("equipos_admin") { EquiposAdminScreen(adminNavController) }
        composable("usuarios_admin") { UsuariosAdminScreen(adminNavController) }

        // Otros paneles
        composable("informes_admin") { InformesAdminScreen(adminNavController) }
        composable("informe_equipos") { InformesEquiposScreen(adminNavController) }
        composable("resultados_informe") {InformeCompScreen(adminNavController)}
        composable("informe_Obras") {
            InformeObrasScreen(
                adminNavController = adminNavController,
                viewModel = viewModel(),
                onResultadosObtenidos = { obrasFiltradas ->
                    // Navegar y pasar las obras filtradas al siguiente screen
                    adminNavController.currentBackStackEntry?.savedStateHandle?.set("obrasFiltradas", obrasFiltradas)
                    adminNavController.navigate("informeCompObraScreen")
                }
            )
        }

        composable("informeCompObraScreen") {
            val obrasFiltradas = adminNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Obra>>("obrasFiltradas")
                ?: emptyList() // por si no llegan datos

            InformeCompObraScreen(
                adminNavController = adminNavController,
                obrasFiltradas = obrasFiltradas,
                viewModel = viewModel()
            )
        }
        composable("informe_Usuarios") { InformeUsuariosScreen(adminNavController)}
        composable("resultados_users") { InformeCompUsers(adminNavController, viewModel = viewModel())}


    }
}
