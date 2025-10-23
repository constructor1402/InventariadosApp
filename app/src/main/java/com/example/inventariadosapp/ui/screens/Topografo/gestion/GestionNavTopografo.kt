package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun GestionNavTopografo(parentNavController: NavController) {
    val navController: NavHostController = rememberNavController()
    var selectedTab by remember { mutableStateOf("asignar") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.fondo_claro),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == "asignar",
                    onClick = {
                        selectedTab = "asignar"
                        navController.navigate("asignar") {
                            popUpTo("asignar") { inclusive = true }
                        }
                    },
                    label = {
                        Text(
                            "Asignar",
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "asignar") Color(0xFF059669)
                            else colorResource(id = R.color.morado_admin)
                        )
                    },
                    icon = {}
                )

                NavigationBarItem(
                    selected = selectedTab == "devolver",
                    onClick = {
                        selectedTab = "devolver"
                        navController.navigate("devolver") {
                            popUpTo("devolver") { inclusive = true }
                        }
                    },
                    label = {
                        Text(
                            "Devolver",
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "devolver") Color(0xFF059669)
                            else colorResource(id = R.color.morado_admin)
                        )
                    },
                    icon = {}
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.fondo_claro))
        ) {
            NavHost(
                navController = navController,
                startDestination = "asignar"
            ) {
                // 🟩 Pantalla principal: Asignar equipo a obra
                composable("asignar") {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.fondo_claro))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Asignar equipo a obra",
                            color = colorResource(id = R.color.texto_principal),
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Pantalla en construcción...",
                            color = colorResource(id = R.color.morado_admin),
                            fontFamily = Kavoon,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // 🔁 Pantalla de devolución (flujo completo con escanear e ingreso manual)
                composable("devolver") {
                    DevolverEquipoScreen(
                        serialArg = "",
                        navController = parentNavController,
                        onScanClick = { parentNavController.navigate("escanear_serial") },
                        onManualClick = { parentNavController.navigate("ingreso_manual") },
                        onConfirmarDevolucion = {
                            parentNavController.navigate("gestion_topografo") {
                                popUpTo("gestion_topografo") { inclusive = true }
                            }
                        }
                    )
                }


            }
        }
    }
}
