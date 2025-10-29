package com.example.inventariadosapp.admin.report.components.models

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes

@Composable
fun ReportSearchScreen(navController: NavController) {
    var nombreCodigo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Informe de equipos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = nombreCodigo,
            onValueChange = { nombreCodigo = it },
            label = { Text("Nombre o código del equipo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val query = nombreCodigo.trim()
                if (query.isNotEmpty()) {
                    // Navegamos pasando el parámetro por la ruta
                    navController.navigate("${ReportRoutes.RESULTS}/${UriEncode(query)}")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
        ) {
            Text("Buscar 🔍", fontSize = 18.sp)
        }
    }
}

/** Utils: codificar parámetros para rutas (evita problemas con espacios/caracteres) */
private fun UriEncode(value: String): String =
    java.net.URLEncoder.encode(value, "UTF-8")
