package com.example.inventariadosapp.screens.admin.gestion

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.EquiposViewModel
import com.example.inventariadosapp.ui.screens.admin.gestion.components.EquipoDropdown
import com.example.inventariadosapp.ui.screens.admin.gestion.components.EquipoTextField
import com.example.inventariadosapp.ui.screens.admin.gestion.components.EquipoUploadButton
import com.example.inventariadosapp.ui.screens.admin.gestion.components.EquiposActionButton
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun EquiposAdminScreen(navController: NavController, viewModel: EquiposViewModel = viewModel()) {
    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔙 Botón volver
            IconButton(
                onClick = { navController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🏷️ Título
                Text(
                    text = "Gestión de Equipos",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                // 🧾 Campos de texto
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EquipoTextField(
                        label = "Serial",
                        placeholder = "Ej. AA12345",
                        value = viewModel.serial,
                        onValueChange = { viewModel.serial = it },
                        modifier = Modifier
                            .weight(1f)
                    )
                    EquipoTextField(
                        label = "Referencia",
                        placeholder = "Ej. AJ-B1",
                        value = viewModel.referencia,
                        onValueChange = { viewModel.referencia = it },
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                // 🔽 Selector tipo de equipo
                EquipoDropdown(
                    label = "Tipo",
                    options = listOf("Herramienta", "Maquinaria", "Vehículo"),
                    selected = viewModel.tipo,
                    onSelectedChange = { viewModel.tipo = it },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.height(20.dp))
                // 📅 Fecha certificación
                EquipoTextField(
                    label = "Fecha Certificación",
                    placeholder = "dd/mm/aaaa",
                    value = viewModel.fecha,
                    onValueChange = { viewModel.fecha = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(60.dp)
                        .fillMaxWidth(0.7f)
                )


                // 📁 Subir archivo
                val context = LocalContext.current
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri ->
                    uri?.let { viewModel.subirCertificado(it) }
                }

                EquipoUploadButton(
                    label = "Certificado",
                    buttonText = "Subir Archivo",
                    onClick = { launcher.launch("application/pdf")},
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                // 🔘 Botones de acción
                EquiposActionButton(
                    text = "Guardar",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Filled.Done,
                    onClick = viewModel::guardarEquipo
                )
                Spacer(modifier = Modifier.height(12.dp))
                EquiposActionButton(
                    text = "Buscar",
                    color = Color(0xFF64B5F6),
                    icon = Icons.Filled.Search,
                    onClick = { viewModel.buscarEquipo() }
                )
                Spacer(modifier = Modifier.height(12.dp))
                EquiposActionButton(
                    text = "Eliminar",
                    color = Color(0xFFE57373),
                    icon = Icons.Filled.Delete,
                    onClick = viewModel::eliminarEquipo
                )
            }
        }
    }
}


