package com.example.inventariadosapp.ui.screens.admin.gestion.equipos

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
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
import com.example.inventariadosapp.ui.screens.admin.gestion.components.CustomTextField
import com.example.inventariadosapp.ui.screens.admin.gestion.components.ActionButtons
import com.example.inventariadosapp.ui.screens.admin.gestion.components.BottomNavGestionBar
import com.example.inventariadosapp.ui.theme.Kavoon
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiposAdminScreen(navController: NavController, viewModel: EquiposViewModel = viewModel()) {
    val context = LocalContext.current
    val mensaje by viewModel.mensaje.collectAsState()
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

    // 📅 Selector de fecha
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.onFechaChange("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // 🎯 Launcher para cargar PDF del certificado
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { data -> viewModel.subirCertificado(data) }
        }
    }



    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {

            // 🔙 Flecha hacia atrás
            IconButton(
                onClick = { navController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
                    .zIndex(2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver al inicio",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 90.dp, bottom = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🏷️ Título
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
                    value = viewModel.serial.collectAsState().value,
                    onValueChange = { viewModel.onSerialChange(it) }
                )


                CustomTextField(
                    label = "Referencia",
                    placeholder = "Ej. AJ-B1",
                    value = viewModel.referencia.collectAsState().value,
                    onValueChange = { viewModel.onReferenciaChange(it) }
                )

                CustomTextField(
                    label = "Descripción",
                    placeholder = "Ej. Estación Topcon, Eslinga, Mazda 14...",
                    value = viewModel.descripcion.collectAsState().value,
                    onValueChange = { viewModel.onDescripcionChange(it) }
                )

                // 🔽 Tipo
                Text(
                    text = "Tipo de equipo",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                val tipoSeleccionado = viewModel.tipo.collectAsState().value
                val textoTipo = if (tipoSeleccionado.isEmpty()) "Seleccione tipo de equipo" else tipoSeleccionado

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    OutlinedTextField(
                        value = viewModel.tipo.collectAsState().value.ifEmpty { "Seleccione tipo de equipo" },
                        onValueChange = {},
                        readOnly = true,
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFBEBFF5),
                            unfocusedContainerColor = Color(0xFFBEBFF5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF000000)
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = if (tipoSeleccionado.isEmpty()) Color(0xFF6D6D6D) else Color.Black,
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
                        // 🔹 Mostrar todos los tipos existentes
                        viewModel.tiposEquipos.collectAsState().value.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion, fontFamily = Kavoon) },
                                onClick = {
                                    viewModel.onTipoChange(opcion)
                                    expanded = false
                                }
                            )
                        }

                        // 🔹 Agregar opción "➕ Otros"
                        DropdownMenuItem(
                            text = { Text("➕ Otros", fontFamily = Kavoon) },
                            onClick = {
                                expanded = false
                                viewModel.onTipoChange("➕ Otros")
                            }
                        )
                    }
                }

// 🔸 Campo para escribir un nuevo tipo (ahora correctamente fuera del menú)
                if (viewModel.tipo.collectAsState().value == "➕ Otros") {
                    var nuevoTipo by remember { mutableStateOf("") }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nuevoTipo,
                        onValueChange = { nuevoTipo = it },
                        label = { Text("Escriba nuevo tipo de equipo") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFBEBFF5),
                            unfocusedContainerColor = Color(0xFFBEBFF5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = Kavoon
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (nuevoTipo.isNotEmpty()) {
                                // Formatear para guardar con mayúscula inicial
                                val tipoFormateado = nuevoTipo.trim().replaceFirstChar { it.uppercase() }
                                viewModel.agregarNuevoTipo(tipoFormateado)
                                nuevoTipo = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.campo_fondo)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(45.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Agregar tipo",
                            tint = Color(0xFF000000)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar nuevo tipo", color = Color(0xFF000000), fontFamily = Kavoon)
                    }
                }


                // 📅 Fecha de certificación
                Text(
                    text = "Fecha Certificación",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Obtenemos la fecha seleccionada del ViewModel
                val fechaSeleccionada = viewModel.fechaCertificacion.collectAsState().value

                Button(
                    onClick = { datePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.campo_fondo)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(52.dp) // un poquito más alto para balance visual
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Seleccionar fecha",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (fechaSeleccionada.isEmpty()) "Seleccionar Fecha" else fechaSeleccionada,
                        color = if (fechaSeleccionada.isEmpty()) Color(0xFF6D6D6D) else Color.Black, // gris o negro según estado
                        fontFamily = Kavoon,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                // 📁 Certificado
                Text(
                    text = "Certificado",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val certificadoUrl = viewModel.certificadoUrl.collectAsState().value

                // 🔹 Si NO hay certificado subido, muestra botón "Subir Archivo"
                if (certificadoUrl.isEmpty()) {
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
                            tint = Color(0xFF000000)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Subir Archivo", color = Color(0xFF000000), fontFamily = Kavoon)
                    }

                    // 🔸 Espacio antes del botón Guardar
                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    // 🔹 Si hay certificado, muestra opciones de Ver y Actualizar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 🔹 Botón VER
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(certificadoUrl))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.campo_fondo)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_save),
                                contentDescription = "Ver certificado",
                                tint = Color(0xFF000000)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver", color = Color(0xFF000000), fontFamily = Kavoon)
                        }

                        // 🔹 Botón ACTUALIZAR
                        Button(
                            onClick = { launcher.launch("application/pdf") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.campo_fondo)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_upload),
                                contentDescription = "Actualizar certificado",
                                tint = Color(0xFF000000)
                            )
                            Spacer(modifier = Modifier.width(9.dp))
                            Text("Actualizar", color = Color(0xFF000000), fontFamily = Kavoon)
                        }
                    }
                }




                // Botones de acción
                ActionButtons(
                    onGuardar = { viewModel.guardarEquipo() },
                    onBuscar = { viewModel.buscarEquipo() },
                    onEliminar = { viewModel.eliminarEquipo() }
                )

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
        }
    }
}



