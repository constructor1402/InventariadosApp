package com.example.inventariadosapp.admin.consulta.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val usuarios = remember { mutableStateListOf<Map<String, String>>() }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("usuarios").get().addOnSuccessListener { result ->
            usuarios.clear()
            for (doc in result) {
                val correo = doc.getString("correoElectronico") ?: ""
                val nombre = doc.getString("nombreCompleto") ?: ""
                if (correo.isNotEmpty()) {
                    usuarios.add(mapOf("correo" to correo, "nombre" to nombre))
                }
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
            title = { Text("Selecciona un Usuario", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3F51B5))
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(
                                    "userReports/${usuario["correo"]}"
                                )
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = usuario["nombre"] ?: "Sin nombre",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = usuario["correo"] ?: "",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

