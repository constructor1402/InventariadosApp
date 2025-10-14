package com.example.inventariadosapp.ui.admin.users_DiegoFaj.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserViewModel


@Composable
fun UserActionButtons(viewModel: UserViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 💾 Guardar
        Button(
            onClick = { viewModel.guardarUsuario() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // verde
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
        ) {
            Icon(Icons.Filled.Save, contentDescription = "Guardar", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Guardar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 🔍 Buscar
        Button(
            onClick = { viewModel.buscarUsuario() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)), // azul
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Buscar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 🗑️ Eliminar
        Button(
            onClick = { viewModel.eliminarUsuario() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)), // rojo
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Eliminar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


