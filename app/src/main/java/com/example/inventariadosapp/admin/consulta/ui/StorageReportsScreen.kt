package com.example.inventariadosapp.admin.consulta.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.R
import com.example.inventariadosapp.admin.consulta.viewmodel.StorageReportsViewModel
import com.google.firebase.storage.FirebaseStorage

@Composable
fun StorageReportsScreen(
    folderPath: String,
    viewModel: StorageReportsViewModel = viewModel()
) {
    var subcarpetaActual by remember { mutableStateOf<String?>(null) }
    val carpetas by viewModel.carpetas.collectAsState()
    val archivos by viewModel.archivos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(folderPath, subcarpetaActual) {
        viewModel.cargarContenido(folderPath, subcarpetaActual)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E7F5))
    ) {
        // 🔹 Barra superior
        TopAppBar(
            title = {
                Text(
                    text = if (subcarpetaActual == null)
                        if (folderPath == "informes_asignaciones") "Reportes de Asignaciones"
                        else "Usuarios en ${folderPath.replace("/", "")}"
                    else
                        "Reportes de ${subcarpetaActual}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                if (subcarpetaActual != null) {
                    IconButton(onClick = { subcarpetaActual = null }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            },
            backgroundColor = Color(0xFF3F51B5),
            contentColor = Color.White,
            elevation = 4.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // 🔹 Mostrar carpetas o archivos
            when {
                carpetas.isNotEmpty() -> {
                    LazyColumn {
                        items(carpetas) { carpeta ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .clickable { subcarpetaActual = carpeta },
                                backgroundColor = Color(0xFFD1C4E9),
                                elevation = 2.dp
                            ) {
                                Text(
                                    text = carpeta,
                                    modifier = Modifier.padding(16.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
                archivos.isNotEmpty() -> {
                    LazyColumn {
                        items(archivos) { archivo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .clickable {
                                        val storage = FirebaseStorage.getInstance()
                                        val ref = if (subcarpetaActual == null)
                                            storage.reference.child("$folderPath/$archivo")
                                        else
                                            storage.reference.child("$folderPath/${subcarpetaActual}/$archivo")
                                        ref.downloadUrl.addOnSuccessListener { uri ->
                                            val intent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()))
                                            context.startActivity(intent)
                                        }
                                    },
                                backgroundColor = Color.White,
                                elevation = 2.dp
                            ) {
                                Text(
                                    text = archivo,
                                    modifier = Modifier.padding(16.dp),
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay elementos en esta carpeta.",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
