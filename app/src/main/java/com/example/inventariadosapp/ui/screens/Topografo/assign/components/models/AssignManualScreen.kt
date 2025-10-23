package com.example.inventariadosapp.ui.screens.Topografo.assign.models

// --- Importaciones Clave ---
import android.widget.Toast //  👈  IMPORTACIÓN AÑADIDA
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll //  👈  IMPORTACIÓN AÑADIDA
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext //  👈  IMPORTACIÓN AÑADIDA
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
    val selectedEquipo = viewModel.selectedEquipo

    //  👇  --- LÓGICA PARA BLOQUEAR CAMPOS ---
    val isAsignado = selectedEquipo.estado.equals("Asignado", ignoreCase = true)

    // Estado para el Dropdown
    var isObrasExpanded by remember { mutableStateOf(false) }
    val obrasList = viewModel.listaDeObras

    //  👇  --- PARA MOSTRAR LOS MENSAJES (TOAST) ---
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        // Barra superior (con tu estilo blanco original)
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
                    text = "Formulario de asignación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
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
        //  👇  --- USAMOS TU 'fondo_claro' ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // Añadido scroll
            verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre campos
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //  👇  --- Mensaje si ya está asignado ---
            if (isAsignado) {
                Text(
                    text = "Este equipo ya está asignado. Debe ser devuelto antes de poder re-asignarlo.",
                    color = colorResource(id = R.color.azul_admin), // Un color que resalte
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.campo_fondo), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            // --- Campo Serial ---
            OutlinedTextField(
                value = selectedEquipo.serial,
                onValueChange = { },
                label = { Text("Serial del equipo") },
                readOnly = true, // Siempre solo lectura
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false // Deshabilitado para que se vea gris
            )

            // --- Campo Tipo ---
            OutlinedTextField(
                value = selectedEquipo.tipo,
                onValueChange = { },
                label = { Text("Tipo de equipo") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false
            )

            // --- Campo Referencia ---
            OutlinedTextField(
                value = selectedEquipo.referencia,
                onValueChange = { },
                label = { Text("Referencia") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false
            )

            // --- Campo Estado ---
            OutlinedTextField(
                value = selectedEquipo.estado,
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
                expanded = isObrasExpanded && !isAsignado, // No se expande si está asignado
                onExpandedChange = {
                    if (!isAsignado) isObrasExpanded = it // No cambia si está asignado
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedEquipo.obra,
                    onValueChange = {},
                    label = { Text("Asignar a obra") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isObrasExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        // Colores de deshabilitado
                        disabledBorderColor = Color.Gray,
                        disabledTextColor = Color.DarkGray,
                        disabledTrailingIconColor = Color.Gray,
                        disabledLabelColor = Color.Gray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    enabled = !isAsignado //  👈  DESHABILITADO SI YA ESTÁ ASIGNADO
                )

                ExposedDropdownMenu(
                    expanded = isObrasExpanded && !isAsignado,
                    onDismissRequest = { isObrasExpanded = false }
                ) {
                    if (obrasList.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Cargando obras...") },
                            onClick = { },
                            enabled = false
                        )
                    } else {
                        obrasList.forEach { obraNombre ->
                            DropdownMenuItem(
                                text = { Text(obraNombre) },
                                onClick = {
                                    viewModel.onObraChange(obraNombre)
                                    isObrasExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Botón Guardar ---
            Button(
                onClick = {
                    viewModel.guardarAsignacion(navController)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.verde_admin),
                    disabledContainerColor = Color.LightGray // Color si está deshabilitado
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(60.dp),
                enabled = !isAsignado //  👈  DESHABILITADO SI YA ESTÁ ASIGNADO
            ) {
                Text(
                    text = "Confirmar asignación",
                    fontFamily = Kavoon,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}