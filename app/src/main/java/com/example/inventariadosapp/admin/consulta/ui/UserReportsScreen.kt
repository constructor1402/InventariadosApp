package com.example.inventariadosapp.admin.consulta.ui

import androidx.compose.ui.res.painterResource
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.inventariadosapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserReportsScreen(navController: NavController, correoUsuario: String) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val informes = remember { mutableStateListOf<Map<String, String>>() }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(correoUsuario) {
        db.collection("informes")
            .whereEqualTo("correoUsuario", correoUsuario)
            .get()
            .addOnSuccessListener { result ->
                informes.clear()
                for (doc in result) {
                    val nombre = doc.getString("nombreArchivo") ?: "Sin nombre"
                    val tipo = doc.getString("tipo") ?: "Desconocido"

                    // 🔹 Detectar si 'fecha' es Timestamp o String
                    val fechaCampo = doc.get("fecha")
                    val fecha = when (fechaCampo) {
                        is com.google.firebase.Timestamp -> {
                            val date = fechaCampo.toDate()
                            val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                            formato.format(date)
                        }
                        is String -> fechaCampo
                        else -> ""
                    }

                    val url = doc.getString("url") ?: ""

                    informes.add(
                        mapOf(
                            "nombre" to nombre,
                            "tipo" to tipo,
                            "fecha" to fecha,
                            "url" to url
                        )
                    )
                }
                loading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E7F5))
    ) {
        TopAppBar(
            title = { Text("Informes de $correoUsuario", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3F51B5))
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (informes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay informes disponibles.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(informes) { informe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(informe["url"]))
                                context.startActivity(intent)
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = informe["nombre"] ?: "Sin nombre",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Tipo: ${informe["tipo"]}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = informe["fecha"] ?: "",
                                fontSize = 13.sp,
                                color = Color(0xFF616161)
                            )
                        }
                    }
                }
            }
        }
    }
}

