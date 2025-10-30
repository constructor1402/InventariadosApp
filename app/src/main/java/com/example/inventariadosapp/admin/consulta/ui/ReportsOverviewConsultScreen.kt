package com.example.inventariadosapp.admin.consulta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.inventariadosapp.admin.consulta.navigation.ConsultRoutes

import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsOverviewConsultScreen(navController: NavController, modifier: Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Informes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3F51B5), // Color de fondo
                    titleContentColor = Color.White,    // Color del título
                    navigationIconContentColor = Color.White // Color del ícono de atrás
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },

    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .background(Color(0xFFE0E7F5)) // Fondo claro
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ReportOptionButton(
                text = "Informe de equipos",
                onClick = {

                    navController.navigate(ReportRoutes.SEARCH)
                },
                containerColor = Color(0xFF3F51B5),
                isEnabled = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Informes de Obras (Deshabilitado para rol Consulta)
            ReportOptionButton(
                text = "Informe de obras",
                onClick = { /* No hay acción */ },
                containerColor = Color(0xFFB0BEC5),
                isEnabled = false
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Informes de Usuarios (Deshabilitado para rol Consulta)
            ReportOptionButton(
                text = "Informe de usuarios",
                onClick = { /* No hay acción */ },
                containerColor = Color(0xFFB0BEC5),
                isEnabled = false
            )
        }
    }
}

@Composable
fun ReportOptionButton(text: String, onClick: () -> Unit, containerColor: Color, isEnabled: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        enabled = isEnabled,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}