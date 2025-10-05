package com.example.inventariadosapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pantalla de Inicio de Sesión")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { navController.navigate("bienvenida") }) {
                    Text("Volver a Inicio")
                }
            }
        }
    }
}

