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
import com.example.inventariadosapp.R

@Composable
fun BottomNavGestionTopografo(
    navController: NavController,
    currentRoute: String
) {
    // 🎨 mismo color que el BottomNavTopografo
    val bgColor = Color(0xFFF5F5F5)

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
                selected = currentRoute == "gestion_topografo",
                onClick = {
                    if (currentRoute != "gestion_topografo") {
                        navController.navigate("gestion_topografo")
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_asignar),
                        contentDescription = "Asignar",
                        tint = Color(0xFF6D20B9), // morado_admin
                        modifier = Modifier.size(32.dp) // 🔺 agranda el ícono
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
                selected = currentRoute == "devolver_equipo",
                onClick = {
                    if (currentRoute != "devolver_equipo") {
                        navController.navigate("devolver_equipo/")
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_devolver),
                        contentDescription = "Devolver",
                        tint = Color(0xFF1E3ABA), // azul_admin
                        modifier = Modifier.size(32.dp) // 🔺 agranda el ícono
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
