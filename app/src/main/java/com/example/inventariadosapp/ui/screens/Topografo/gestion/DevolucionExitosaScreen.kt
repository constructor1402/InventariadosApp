package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DevolucionExitosaScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "El equipo ha sido devuelto correctamente")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            // volver al inicio de topografo o al stack previo
            navController.popBackStack()
            navController.popBackStack() // asegurar volver al main si es necesario
        }) {
            Text(text = "Volver")
        }
    }
}


