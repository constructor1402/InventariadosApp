package com.example.inventariadosapp.admin.topografo.assign.components.models

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.admin.topografo.assign.components.models.TopografoAssignViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignManualScreen(
    navController: NavController, // 👈 agregado para navegación
    viewModel: TopografoAssignViewModel = viewModel()
) {
    val serial by viewModel.serial.collectAsState()
    val referencia by viewModel.referencia.collectAsState()
    val tipo by viewModel.tipo.collectAsState()
    val obra by viewModel.obra.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val error by viewModel.error.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asignar Equipo a Obra", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7986CB)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFFDDE6FF)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = serial,
                onValueChange = viewModel::updateSerial,
                label = { Text("Serial del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = referencia,
                onValueChange = viewModel::updateReferencia,
                label = { Text("Referencia del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = tipo,
                onValueChange = viewModel::updateTipo,
                label = { Text("Tipo de equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = obra,
                onValueChange = viewModel::updateObra,
                label = { Text("Obra") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.buscarEquipo(serial) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF64B5F6))
                ) {
                    Text("Buscar")
                }

                Button(
                    onClick = { viewModel.guardarAsignacion() },
                    colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                ) {
                    Text("Guardar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mensaje.isNotEmpty()) {
                Text(text = mensaje, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
            }

            if (error.isNotEmpty()) {
                Text(text = error, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
            }
        }
    }
}

