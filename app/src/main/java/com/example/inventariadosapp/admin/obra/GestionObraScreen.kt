package com.example.inventariadosapp.admin.obra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.admin.obra.components.ObraTextField
import com.example.inventariadosapp.admin.obra.components.ObraActionButton
import com.example.inventariadosapp.screens.admin.gestion.ObraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionObraScreen(
    viewModel: ObraViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val backgroundColor = Color(0xFFDDE6FF) // Azul pastel claro

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Obras",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif // Cambia por Kavoon si ya la importaste
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7986CB)
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Gestión de Obras",
                style = TextStyle(
                    fontFamily = FontFamily.Serif, // Reemplazar por Kavoon si deseas
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campos de texto
            ObraTextField(
                value = viewModel.nombreObra,
                onValueChange = viewModel::updateNombreObra,
                label = "Nombre de Obra",
                isError = viewModel.nombreObraError,
                errorMessage = if (viewModel.nombreObraError) "Escribe el nombre de la obra" else null
            )

            ObraTextField(
                value = viewModel.ubicacion,
                onValueChange = viewModel::updateUbicacion,
                label = "Ubicación",
                isError = viewModel.ubicacionError,
                errorMessage = if (viewModel.ubicacionError) "Ingresa la ubicación" else null
            )

            ObraTextField(
                value = viewModel.clienteNombre.ifBlank { "Cliente asociado" },
                onValueChange = {},
                label = "Cliente",
                isError = false,
                errorMessage = null,
                readOnly = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Fila con botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ObraActionButton(
                    text = "Guardar",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Filled.Done, // ✅ Cambiado a ImageVector
                    onClick = viewModel::guardarObra,
                )
                ObraActionButton(
                    text = "Buscar",
                    color = Color(0xFF64B5F6),
                    icon = Icons.Filled.Search, // ✅ Cambiado a ImageVector
                    onClick = { viewModel.buscarObra(viewModel.nombreObra) },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Eliminar centrado
            ObraActionButton(
                text = "Eliminar",
                color = Color(0xFFE57373),
                icon = Icons.Filled.Delete, // ✅ Cambiado a ImageVector
                onClick = viewModel::eliminarObra,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de estado (éxito/error)
            viewModel.mensajeStatus?.let { msg ->
                Text(
                    text = msg,
                    color = if (msg.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
