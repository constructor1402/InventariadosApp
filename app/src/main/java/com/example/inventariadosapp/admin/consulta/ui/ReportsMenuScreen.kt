package com.example.inventariadosapp.admin.consulta.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R

@Composable
fun ReportsMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E7F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecciona el tipo de reporte",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        ReportOptionCard(
            title = "Reportes de Usuarios",
            description = "Ver reportes agrupados por usuario (carpeta 'informes')",
            iconRes = R.drawable.ic_document,
            color = Color(0xFF3F51B5)
        ) {
            navController.navigate("usersList")
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReportOptionCard(
            title = "Reportes de Asignaciones",
            description = "Ver reportes directos en la carpeta 'informes_asignaciones'",
            iconRes = R.drawable.ic_list,
            color = Color(0xFF9C27B0)
        ) {
            navController.navigate("storageReports/informes_asignaciones")
        }
    }
}

@Composable
fun ReportOptionCard(
    title: String,
    description: String,
    iconRes: Int,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        backgroundColor = color,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}
