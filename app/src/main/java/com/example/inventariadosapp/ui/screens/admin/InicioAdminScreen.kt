package com.example.inventariadosapp.screens.admin

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
fun InicioAdminScreen(
    adminNavController: NavController,
    mainNavController: NavController
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(adminNavController, currentRoute = "inicio_admin") }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // ❌ Botón cerrar sesión → login principal
            IconButton(
                onClick = {
                    mainNavController.navigate("login") {
                        popUpTo("panel_admin") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_roja),
                    contentDescription = "Cerrar sesión",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Contenido principal centrado verticalmente
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 80.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vista general",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                // 📊 Tarjetas distribuidas verticalmente
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    DashboardCard(Color(0xFF3949AB), "8", "Equipos Disponibles")
                    DashboardCard(Color(0xFF7B1FA2), "12", "Equipos en Uso")
                    DashboardCard(Color(0xFF43A047), "5", "Usuarios Creados")
                }
            }

        }
    }
}

@Composable
fun DashboardCard(color: Color, numero: String, descripcion: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(220.dp)
            .height(90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = numero,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = descripcion,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            )
        }
    }
}
