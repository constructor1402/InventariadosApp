package com.example.inventariadosapp.admin.report.components.models

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import com.example.inventariadosapp.admin.report.repository.ReportRepository
import com.google.firebase.Timestamp
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportResultsScreen(
    navController: NavController,
    codigoBuscado: String
) {
    val firestore = FirebaseFirestore.getInstance()
    var asignacion by remember { mutableStateOf<Map<String, Any>?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var notFound by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(codigoBuscado) {
        cargando = true
        notFound = false
        asignacion = null
        val query = codigoBuscado.trim().uppercase()

        firestore.collection("asignaciones")
            .whereEqualTo("serial", query)
            .get()
            .addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    asignacion = snap.documents.first().data
                    cargando = false
                } else {
                    notFound = true
                    cargando = false
                }
            }
            .addOnFailureListener {
                notFound = true
                cargando = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informe de Asignación") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack(
                            route = ReportRoutes.SEARCH,
                            inclusive = false
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a búsqueda"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Resultados del Informe de Asignación",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                cargando -> CircularProgressIndicator()
                notFound -> Text(
                    "No se encontró una asignación con ese serial.",
                    color = MaterialTheme.colorScheme.error
                )
                asignacion != null -> {
                    val fecha = (asignacion!!["fechaAsignacion"] as? Timestamp)?.toDate()
                    val fechaStr = fecha?.let {
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
                    } ?: "Sin fecha"

                    // Mostrar datos
                    Text("Serial: ${asignacion!!["serial"]}", fontSize = 16.sp)
                    Text("Tipo: ${asignacion!!["tipo"]}", fontSize = 16.sp)
                    Text("Referencia: ${asignacion!!["referencia"]}", fontSize = 16.sp)
                    Text("Obra Asignada: ${asignacion!!["obraAsignada"]}", fontSize = 16.sp)
                    Text("Estado Previo: ${asignacion!!["estadoPrevio"]}", fontSize = 16.sp)
                    Text("Fecha Asignación: $fechaStr", fontSize = 16.sp)
                    Text("Usuario: ${asignacion!!["usuarioNombre"]}", fontSize = 16.sp)
                    Text("Correo: ${asignacion!!["usuarioEmail"]}", fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            ReportRepository.generarPDFAsignaciones(
                                context = context,
                                asignaciones = listOf(asignacion!!),
                                nombreInforme = "Informe_${codigoBuscado}"
                            ) { url ->
                                if (url != null) {
                                    Toast.makeText(context, "Informe subido con éxito", Toast.LENGTH_SHORT).show()
                                    Log.d("PDF", "URL de descarga: $url")
                                } else {
                                    Toast.makeText(context, "Error al subir el informe", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Generar Informe", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}