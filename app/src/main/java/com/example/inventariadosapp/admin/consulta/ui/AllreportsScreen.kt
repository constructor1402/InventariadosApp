package com.example.inventariadosapp.admin.consulta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.admin.consulta.models.ReportItem
import com.example.inventariadosapp.admin.consulta.viewmodel.ConsultViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.ArrowBack

// Formato de fecha para la UI
private val dateFormat = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllReportsScreen(
    navController: NavController,
    viewModel: ConsultViewModel
) {
    // Escucha la lista de TODOS los informes generados
    val reports by viewModel.allReports.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todos los Informes Generados") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE0E7F5))
                .padding(horizontal = 16.dp, vertical = 8.dp) // Ajuste para el padding
        ) {
            if (reports.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cargando o no se han encontrado informes generados.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Text(
                            text = "Total de informes: ${reports.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(reports, key = { it.id }) { report ->
                        ReportDetailCard(
                            item = report,
                            onDownloadClick = {
                                // TODO: Implementar lógica de descarga del PDF
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun ReportDetailCard(item: ReportItem, onDownloadClick: (ReportItem) -> Unit) {
    // Definir formato de fecha
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Título/Descripción
                Text(
                    text = item.descripcion ?: "Informe sin descripción",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Detalles del Informe
            // Usamos 'codigo' o 'serial' dependiendo de cuál exista
            DetailRow(label = "Cód/Serial:", value = item.codigo ?: item.serial ?: "N/A")
            DetailRow(label = "Referencia:", value = item.referencia ?: "N/A")
            DetailRow(label = "Tipo:", value = item.tipo ?: "N/A")

            // Fecha Certificación
            DetailRow(label = "F. Certificación:", value = item.fechaCertificacion ?: "N/A")

            // Fecha de Generación Unificada
            // Usamos fechaReporte (la unificada que creamos en el ViewModel)
            val formattedDate = item.fechaReporte?.toDate()?.let { formatter.format(it) } ?: "Fecha N/A"
            DetailRow(label = "F. Generación:", value = formattedDate)

            // Fuente (Ayuda para depurar si faltan informes)
            DetailRow(label = "Fuente Colección:", value = item.fuenteColeccion)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

        Text(text = value, fontSize = 14.sp, maxLines = 2)
    }
}