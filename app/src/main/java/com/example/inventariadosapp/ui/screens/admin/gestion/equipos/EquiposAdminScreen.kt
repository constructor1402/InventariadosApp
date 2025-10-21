package com.example.inventariadosapp.screens.admin.gestion

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.equipos.EquiposViewModel
import com.example.inventariadosapp.ui.screens.admin.gestion.components.ActionButton
import com.example.inventariadosapp.ui.screens.admin.gestion.components.CustomTextField
import com.example.inventariadosapp.ui.theme.Kavoon
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiposAdminScreen(navController: NavController, viewModel: EquiposViewModel = viewModel()) {
    val context = LocalContext.current
    val mensaje by viewModel.mensaje.collectAsState()
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) } // Dropdown expandido o no

    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // Contenido principal scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 90.dp, bottom = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Equipos",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campos de texto
                CustomTextField(
                    label = "Serial",
                    placeholder = "Ej. AA12345",
                    value = viewModel.serial,
                    onValueChange = { viewModel.serial = it }
                )

                CustomTextField(
                    label = "Referencia",
                    placeholder = "Ej. AJ-B1",
                    value = viewModel.referencia,
                    onValueChange = { viewModel.referencia = it }
                )

                // 🔽 Campo desplegable de Tipo
                val opciones = listOf("Herramienta", "Maquinaria", "Vehículo")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tipo",
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        OutlinedTextField(
                            value = viewModel.tipo.ifEmpty { "Seleccione tipo de equipo" },
                            onValueChange = {},
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEDEDED),
                                unfocusedContainerColor = Color(0xFFEDEDED),
                                focusedIndicatorColor = colorResource(id = R.color.boton_principal),
                                unfocusedIndicatorColor = Color.Gray,
                                cursorColor = colorResource(id = R.color.boton_principal)
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = Kavoon,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .height(52.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            opciones.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, fontFamily = Kavoon) },
                                    onClick = {
                                        viewModel.tipo = opcion
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // 📅 Selección de fecha solo con botón de calendario
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val fechaSeleccionada = "%02d/%02d/%d".format(
                            selectedDay, selectedMonth + 1, selectedYear
                        )
                        viewModel.fecha = fechaSeleccionada
                    },
                    year, month, day
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Fecha Certificación",
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.fecha.ifEmpty { "Sin seleccionar" },
                        onValueChange = {},
                        readOnly = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFEDEDED),
                            unfocusedContainerColor = Color(0xFFEDEDED),
                            focusedIndicatorColor = colorResource(id = R.color.boton_principal),
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = Kavoon,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(52.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { datePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.campo_fondo)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(42.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Seleccionar fecha",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Seleccionar Fecha", color = Color.Black, fontFamily = Kavoon)
                    }
                }

                // 📁 Subir archivo
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri ->
                    uri?.let { viewModel.subirCertificado(it) }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Certificado",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.campo_fondo)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "Subir archivo",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Subir Archivo",
                        color = Color.Black,
                        fontFamily = Kavoon
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton("Guardar", Color(0xFF4CAF50), R.drawable.ic_check_circle) {
                        viewModel.guardarEquipo()
                    }
                    ActionButton("Buscar", Color(0xFF2196F3), R.drawable.ic_search) {
                        viewModel.buscarEquipo()
                    }
                    ActionButton("Eliminar", Color(0xFFE53935), R.drawable.ic_delete) {
                        viewModel.eliminarEquipo()
                    }
                }

                // Mensaje dinámico
                if (mensaje.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = mensaje,
                        color = when {
                            mensaje.contains("✅") -> Color(0xFF2E7D32)
                            mensaje.contains("🗑️") -> Color(0xFFE53935)
                            mensaje.contains("⚠️") -> Color(0xFFFFA000)
                            else -> Color(0xFFD32F2F)
                        },
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }

            // 🔙 Flecha hacia atrás (con zIndex para que no quede tapada)
            IconButton(
                onClick = { navController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
                    .zIndex(2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
