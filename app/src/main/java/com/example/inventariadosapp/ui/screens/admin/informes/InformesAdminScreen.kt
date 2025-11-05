package com.example.inventariadosapp.ui.screens.admin.informes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformesAdminScreen(
    navController: NavController,
    userCorreo: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel de Informes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Selecciona el tipo de informe a generar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = { navController.navigate("informe_equipos/$userCorreo") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📋 Informe de Equipos", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate("informe_obras/$userCorreo") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🏗️ Informe de Obras", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate("informe_usuarios/$userCorreo") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("👤 Informe de Usuarios", fontSize = 16.sp)
            }
        }
    }
}
