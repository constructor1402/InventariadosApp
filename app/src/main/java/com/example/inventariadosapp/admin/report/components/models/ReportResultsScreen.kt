package com.example.inventariadosapp.admin.report.components.models

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportResultsScreen(
    navController: NavController,
    codigoBuscado: String
) {
    val firestore = FirebaseFirestore.getInstance()
    var equipo by remember { mutableStateOf<Map<String, Any>?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var notFound by remember { mutableStateOf(false) }

    LaunchedEffect(codigoBuscado) {
        cargando = true
        notFound = false
        equipo = null
        val query = codigoBuscado.trim().uppercase()


        firestore.collection("equipos")
            .whereEqualTo("serial", query)
            .get()
            .addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    equipo = snap.documents.first().data
                    cargando = false
                } else {
                    firestore.collection("equipos").document(query).get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                equipo = doc.data
                            } else {
                                notFound = true
                            }
                            cargando = false
                        }
                        .addOnFailureListener {
                            notFound = true
                            cargando = false
                        }
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
                title = { Text("Resultados de: $codigoBuscado") },
                navigationIcon = {
                    IconButton(onClick = {

                        navController.popBackStack(
                            route = ReportRoutes.SEARCH,
                            inclusive = false
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a Búsqueda"
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
                text = "Resultados del Informe",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                cargando -> CircularProgressIndicator()
                notFound -> {
                    Text("No se encontró el equipo.", color = MaterialTheme.colorScheme.error)
                }
                equipo != null -> {
                    // Mostrar datos del equipo encontrado
                    Text("Serial: ${equipo!!["serial"]}", fontSize = 16.sp)
                    Text("Referencia: ${equipo!!["referencia"]}", fontSize = 16.sp)
                    Text("Tipo: ${equipo!!["tipo"]}", fontSize = 16.sp)
                    Text("Descripción: ${equipo!!["descripcion"]}", fontSize = 16.sp)
                    Text("Fecha Certificación: ${equipo!!["fechaCertificacion"]}", fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón: navegar a Success
                    Button(
                        onClick = {
                            val encoded = java.net.URLEncoder.encode(codigoBuscado, "UTF-8")
                            navController.navigate("${ReportRoutes.SUCCESS}/$encoded")
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