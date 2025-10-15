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
fun InicioAdminScreen(adminNavController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(adminNavController, currentRoute = "inicio_admin") }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // ❌ Botón Cerrar sesión → vuelve al Login principal
            IconButton(
                onClick = {
                    // ✅ Este comando devuelve al flujo principal (AppNavigation)
                    adminNavController.navigate("login") {
                        popUpTo("inicio_admin") { inclusive = true } // limpia el stack interno
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

            // 🔹 Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vista general",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                DashboardCard(
                    color = Color(0xFF3949AB),
                    numero = "8",
                    descripcion = "Equipos Disponibles"
                )
                Spacer(modifier = Modifier.height(24.dp))
                DashboardCard(
                    color = Color(0xFF7B1FA2),
                    numero = "12",
                    descripcion = "Equipos en Uso"
                )
                Spacer(modifier = Modifier.height(24.dp))
                DashboardCard(
                    color = Color(0xFF43A047),
                    numero = "5",
                    descripcion = "Usuarios Creados"
                )
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
