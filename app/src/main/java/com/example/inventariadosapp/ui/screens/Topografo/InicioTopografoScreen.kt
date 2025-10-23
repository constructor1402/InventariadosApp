package com.example.inventariadosapp.ui.screens.Topografo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun InicioTopografoScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavTopografo(
                navController = navController,
                currentRoute = navController.currentDestination?.route ?: ""
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔴 Botón cerrar sesión (X roja)
            IconButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("panel_topografo") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_roja),
                    contentDescription = "Cerrar sesión",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 🏗️ Título principal
                Text(
                    text = "Vista general",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🟪 Tarjeta de equipos disponibles
                Card(
                    modifier = Modifier
                        .width(190.dp)
                        .height(110.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.morado_admin)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_obras),
                            contentDescription = "Equipos disponibles",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "8",
                            color = Color.White,
                            fontFamily = Kavoon,
                            fontSize = 28.sp
                        )

                        Text(
                            text = "Equipos Disponibles",
                            color = Color.White,
                            fontFamily = Kavoon,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 🟩 Subtítulo
                Text(
                    text = "Accesos Rápidos",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🟢 Botón Asignar equipo a obra
                Button(
                    onClick = { navController.navigate("gestion_topografo") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF34A853)
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Asignar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Asignar Equipo a Obra",
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔴 Botón Devolver equipo
                Button(
                    onClick = { navController.navigate("devolver_equipo/") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA4335)
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Devolver",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Devolver Equipo",
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
