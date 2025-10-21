package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun BottomNavGestionBar(navController: NavController) {
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color(0xFF2C2C2C) else Color(0xFFF5F5F5)
    val textColor = if (darkTheme) Color.White else Color.Black

    Surface(
        shadowElevation = 12.dp,
        color = backgroundColor,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🏗️ Obras
            GestionNavItem(
                icon = R.drawable.ic_obras,
                label = "Obras",
                route = "obras_admin",
                navController = navController,
                textColor = textColor
            )

            // ⚙️ Equipos
            GestionNavItem(
                icon = R.drawable.ic_equipment,
                label = "Equipos",
                route = "equipos_admin",
                navController = navController,
                textColor = textColor
            )

            // 👥 Usuarios
            GestionNavItem(
                icon = R.drawable.ic_users,
                label = "Usuarios",
                route = "usuarios_admin",
                navController = navController,
                textColor = textColor
            )
        }
    }
}

@Composable
fun GestionNavItem(
    icon: Int,
    label: String,
    route: String,
    navController: NavController,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { navController.navigate(route) }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = colorResource(id = R.color.boton_principal),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Kavoon
        )
    }
}
