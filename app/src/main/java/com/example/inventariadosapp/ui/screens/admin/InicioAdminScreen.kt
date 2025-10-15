package com.example.inventariadosapp.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun InicioAdminScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar() } // 👈 Agregamos la barra inferior
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

                // 🔹 Tarjetas del tablero
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
            .padding(horizontal = 8.dp),
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

@Composable
fun BottomNavigationBar() {
    Surface(
        shadowElevation = 12.dp,
        tonalElevation = 3.dp,
        color = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(icon = R.drawable.ic_home, label = "Inicio")
            BottomNavItem(icon = R.drawable.ic_gestion, label = "Gestión")
            BottomNavItem(icon = R.drawable.ic_informes, label = "Informes")
        }
    }
}

@Composable
fun BottomNavItem(icon: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = colorResource(id = R.color.boton_principal).copy(alpha = 0.25f),
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = colorResource(id = R.color.boton_principal),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = colorResource(id = R.color.texto_principal),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontFamily = Kavoon
        )
    }
}


