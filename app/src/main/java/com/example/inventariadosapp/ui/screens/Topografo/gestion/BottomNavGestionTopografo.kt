package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.inventariadosapp.R

@Composable
fun BottomNavGestionTopografo(
    navController: NavController,
    currentRoute: String
) {
    val bgColor = Color(0xFFF5F5F5)

    // --- Definimos las rutas correctas aquí ---
    val assignRoute = "gestion_topografo"
    // La ruta de devolver necesita un argumento, pero como es la principal,
    // le pasamos un argumento vacío (que tu NavGraph ya maneja).
    val returnRoute = "devolver_equipo/"


    Surface(
        color = bgColor,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(8.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(bgColor)
    ) {
        NavigationBar(
            containerColor = bgColor,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            // 🔹 Asignar
            NavigationBarItem(
                //  👇 --- CORRECCIÓN AQUÍ ---
                selected = currentRoute == assignRoute,
                onClick = {
                    //  👇 --- LÓGICA DE NAVEGACIÓN CORREGIDA ---
                    if (currentRoute != assignRoute) {
                        navController.navigate(assignRoute) {
                            // Limpia el backstack hasta el inicio del grafo
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Evita múltiples copias de la misma pantalla
                            launchSingleTop = true
                            // Restaura el estado al volver
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_asignar),
                        contentDescription = "Asignar",
                        tint = Color(0xFF6D20B9),
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(
                        text = "Asignar",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )

            // 🔸 Devolver
            NavigationBarItem(
                //  👇 --- CORRECCIÓN AQUÍ (para que coincida con la lógica) ---
                selected = currentRoute.startsWith("devolver_equipo"),
                onClick = {
                    //  👇 --- LÓGICA DE NAVEGACIÓN CORREGIDA ---
                    if (!currentRoute.startsWith("devolver_equipo")) {
                        navController.navigate(returnRoute) { // Ruta ya incluye "/"
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_devolver),
                        contentDescription = "Devolver",
                        tint = Color(0xFF1E3ABA),
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(
                        text = "Devolver",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}
