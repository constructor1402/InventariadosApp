package com.example.inventariadosapp.ui.screens.Topografo.assign.models

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignManualScreen(navController: NavHostController, viewModel: TopografoAssignViewModel) {
    val context = LocalContext.current
    val selectedEquipo = viewModel.selectedEquipo
    var manualSerial by remember { mutableStateOf(selectedEquipo.serial) }
    var manualTipo by remember { mutableStateOf(selectedEquipo.tipo) }
    var manualReferencia by remember { mutableStateOf(selectedEquipo.referencia) }

    var isObrasExpanded by remember { mutableStateOf(false) }
    val obrasList = viewModel.listaDeObras
    val isAsignado = selectedEquipo.estado.equals("Asignado", ignoreCase = true)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Formulario de asignación manual",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_roja),
                        contentDescription = "Cerrar",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Serial manual ---
            OutlinedTextField(
                value = manualSerial,
                onValueChange = { manualSerial = it.uppercase() },
                label = { Text("Serial del equipo") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.boton_principal)
                )
            )

            Button(
                onClick = {
                    if (manualSerial.isNotBlank()) {
                        viewModel.fetchEquipoData(manualSerial) { success ->
                            if (success) {
                                manualTipo = viewModel.selectedEquipo.tipo
                                manualReferencia = viewModel.selectedEquipo.referencia
                                Toast.makeText(context, "Equipo encontrado ✅", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "No encontrado, ingrese manualmente los datos", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Ingrese un serial válido", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.azul_admin)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Buscar equipo", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // --- Tipo ---
            OutlinedTextField(
                value = manualTipo,
                onValueChange = { manualTipo = it },
                label = { Text("Tipo de equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Referencia ---
            OutlinedTextField(
                value = manualReferencia,
                onValueChange = { manualReferencia = it },
                label = { Text("Referencia") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Estado actual ---
            OutlinedTextField(
                value = if (selectedEquipo.estado.isNotBlank()) selectedEquipo.estado else "Disponible",
                onValueChange = { },
                label = { Text("Estado actual") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false
            )

            // --- Dropdown Obras ---
            ExposedDropdownMenuBox(
                expanded = isObrasExpanded,
                onExpandedChange = { isObrasExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedEquipo.obra.ifBlank { "Seleccione la obra" },
                    onValueChange = {},
                    label = { Text("Asignar a obra") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isObrasExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isObrasExpanded,
                    onDismissRequest = { isObrasExpanded = false }
                ) {
                    obrasList.forEach { obra ->
                        DropdownMenuItem(
                            text = { Text(obra) },
                            onClick = {
                                viewModel.onObraChange(obra)
                                isObrasExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Guardar asignación ---
            Button(
                onClick = {
                    // ✅ Si no vino de Firebase, actualizamos manualmente los valores
                    if (viewModel.selectedEquipo.serial.isBlank()) {
                        viewModel.setSelectedEquipo(
                            EquipoAsignado(
                                serial = manualSerial,
                                tipo = manualTipo,
                                referencia = manualReferencia,
                                obra = viewModel.selectedEquipo.obra
                            )
                        )

                    }
                    viewModel.guardarAsignacion(navController)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde_admin)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
            ) {
                Text("Confirmar asignación", fontFamily = Kavoon, color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

private fun TopografoAssignViewModel.setSelectedEquipo(
    equipoAsignado: EquipoAsignado
) {
}
