package com.example.inventariadosapp.admin.consulta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.inventariadosapp.admin.consulta.navigation.ConsultRoutes
import com.example.inventariadosapp.admin.consulta.viewmodel.ConsultViewModel
import com.example.inventariadosapp.admin.consulta.models.MetricData

import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes




sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    // 1.  pantalla de inicio
    object Inicio : BottomNavItem(ConsultRoutes.HOME, Icons.Default.Home, "Inicio")

    // 2. La pantalla de búsqueda de informes
    object Informes : BottomNavItem(ReportRoutes.SEARCH, Icons.Default.List, "Informes")
}

val bottomNavItems = listOf(BottomNavItem.Inicio, BottomNavItem.Informes)


@Composable
fun HomeScreenConsult(
    navController: NavController,
    viewModel: ConsultViewModel
) {
    val metricas by viewModel.metricas.collectAsState(initial = MetricData())

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                // Obtenemos la ruta actual para saber qué ítem seleccionar
                val currentRoute = navController.currentBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    // Comprueba si la ruta actual es la ruta del ítem
                    val isSelected = currentRoute == item.route

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp)) },
                        label = { Text(item.label, fontSize = 12.sp) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                // Navegamos a la ruta deseada
                                navController.navigate(item.route) {
                                    // Limpiamos el back stack hasta el inicio del grafo
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true // Guardar el estado de la pantalla anterior (Inicio)
                                    }
                                    launchSingleTop = true // Evitar múltiples copias
                                    restoreState = true // Restaurar si ya estaba en el stack
                                }
                            }
                        })
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE0E7F5))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Flecha de retroceso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
                Spacer(Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título y Métricas (Resumen general)
            Text(
                text = "Resumen general",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de métricas
            MetricCard(
                value = metricas.equiposDisponibles.toString(),
                description = "Equipos Disponibles",
                cardBackgroundColor = Color(0xFF3F51B5), // Azul fuerte
                icon = Icons.Default.Description
            )
            Spacer(modifier = Modifier.height(16.dp))
            MetricCard(
                value = metricas.equiposEnUso.toString(),
                description = "Equipos en Uso",
                cardBackgroundColor = Color(0xFF9C27B0), // Púrpura
                icon = Icons.Default.List
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Acceso Rápido y Botón
            Text(
                text = "Acceso Rápidos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón "Ver Reportes Detallados"
            Button(
                onClick = {
                    navController.navigate(ConsultRoutes.ALL_REPORTS)
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ver Reportes Detallados",
                        color = Color(0xFF3F51B5),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Reportes",
                        tint = Color(0xFF3F51B5),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
fun MetricCard(value: String, description: String, cardBackgroundColor: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {

    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = description,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}