package com.example.inventariadosapp.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.InicioAdminViewModel
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun InicioAdminScreen(
    adminNavController: NavController,
    mainNavController: NavController,
    userCorreo: String
) {
    val darkTheme = isSystemInDarkTheme()

    Scaffold(
        bottomBar = {
            val bgColor = if (darkTheme) Color(0xFF2C2C2C) else Color(0xFFF5F5F5)
            val textColor = if (darkTheme) Color.White else Color.Black
            BottomNavigationBar(adminNavController, "inicio_admin", bgColor, textColor)
        }
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

                // 🔹 ViewModel y estados
                val viewModel: InicioAdminViewModel = viewModel()
                val equiposDisponibles by viewModel.equiposDisponibles.collectAsState()
                val equiposAsignados by viewModel.equiposAsignados.collectAsState()
                val usuariosCreados by viewModel.usuariosCreados.collectAsState()

                // 📊 Tarjetas distribuidas verticalmente
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    DashboardCard(Color(0xFF3949AB), equiposDisponibles.toString(), "Equipos Disponibles")
                    DashboardCard(Color(0xFF7B1FA2), equiposAsignados.toString(), "Equipos Asignados")
                    DashboardCard(Color(0xFF43A047), usuariosCreados.toString(), "Usuarios Creados")
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

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        color = bgColor,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp,
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
            // Opciones de navegación
            BottomNavItem(
                icon = R.drawable.ic_home,
                label = "Inicio",
                isSelected = currentRoute == "inicio_admin",
                onClick = { if (currentRoute != "inicio_admin") navController.navigate("inicio_admin") },
                textColor = textColor
            )
            BottomNavItem(
                icon = R.drawable.ic_gestion,
                label = "Gestión",
                isSelected = currentRoute == "gestion_admin",
                onClick = { if (currentRoute != "gestion_admin") navController.navigate("gestion_admin") },
                textColor = textColor
            )
            BottomNavItem(
                icon = R.drawable.ic_informes,
                label = "Informes",
                isSelected = currentRoute == "informes_admin",
                onClick = { if (currentRoute != "informes_admin") navController.navigate("informes_admin") },
                textColor = textColor
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    textColor: Color
) {
    val backgroundColor = if (isSelected)
        colorResource(id = R.color.boton_principal).copy(alpha = 0.3f)
    else
        colorResource(id = R.color.boton_principal).copy(alpha = 0.1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Surface(
            color = backgroundColor,
            shape = MaterialTheme.shapes.medium,
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
            color = textColor,
            fontSize = 12.sp,
            fontFamily = Kavoon,
            textAlign = TextAlign.Center
        )
    }
}
