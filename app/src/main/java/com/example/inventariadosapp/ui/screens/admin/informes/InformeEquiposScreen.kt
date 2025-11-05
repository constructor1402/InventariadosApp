package com.example.inventariadosapp.ui.screens.admin.informes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.ui.screens.admin.informes.viewmodels.InformeEquiposViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeEquiposScreen(
    navController: NavController,
    userCorreo: String,
    viewModel: InformeEquiposViewModel = viewModel()
) {
    val equipos by viewModel.equipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Informe de Equipos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                if (equipos.isEmpty()) {
                    Text(
                        text = "No hay equipos registrados.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Listado de Equipos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    equipos.forEach { equipo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Serial: ${equipo.serial}", fontWeight = FontWeight.Bold)
                                Text("Referencia: ${equipo.referencia}")
                                Text("Tipo: ${equipo.tipo}")
                                Text("Estado: ${equipo.estado}")
                                Text("Fecha Certificación: ${equipo.fechaCertificacion}")
                                Text("Descripción: ${equipo.descripcion}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val coroutineScope = rememberCoroutineScope()

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val filePath = viewModel.generarInformePDFequipos(equipos, userCorreo)
                                if (!filePath.startsWith("Error")) {
                                    viewModel.guardarInformeEnFirebase(filePath, "equipos", userCorreo)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "📄 Generar y subir informe PDF", fontSize = 16.sp)
                    }


                }
            }
        }
    }
}
