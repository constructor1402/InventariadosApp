package com.example.inventariadosapp.admin.topografo.assign.components.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla principal del módulo de Asignar Equipo a Obra.
 * Permite elegir entre escanear con cámara o ingresar datos manualmente.
 */
@Composable
fun AssignMenuScreen(
    onNavigateToManual: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6E0FF)) // azul claro de fondo
            .padding(horizontal = 20.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = "Asignar Equipo a Obra",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        // Botón de escaneo con cámara
        Button(
            onClick = { onNavigateToCamera() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // verde
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "Escanear con cámara",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Botón de ingreso manual
        Button(
            onClick = { onNavigateToManual() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)), // azul
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "Ingresar Datos Manual",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
