package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.net.URLDecoder

@Composable
fun IngresoManualScreen(
    navController: NavController,
    serialArg: String?,
    repo: FirebaseDevolucionRepository = FirebaseDevolucionRepository()
) {
    val scope = rememberCoroutineScope()
    var serial by remember { mutableStateOf(serialArg ?: "") }
    var referencia by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var equipoId by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Si se viene con serial precargado (desde el escáner)
    LaunchedEffect(serialArg) {
        if (!serialArg.isNullOrBlank()) {
            serial = URLDecoder.decode(serialArg, "utf-8")
            loading = true
            val eq = try { repo.buscarPorSerial(serial) } catch (e: Exception) { null }
            if (eq != null) {
                equipoId = eq.id
                referencia = eq.referencia ?: ""
                tipo = eq.tipo ?: ""
                statusMessage = null
            } else {
                statusMessage = "Equipo no encontrado"
            }
            loading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDCE6FA))
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Devolver Equipo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E8E)
                    ),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = serial,
                    onValueChange = { serial = it },
                    label = { Text("Serial del Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = referencia,
                    onValueChange = { referencia = it },
                    label = { Text("Referencia del Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                loading = true
                                statusMessage = null
                                val eq = try { repo.buscarPorSerial(serial) } catch (e: Exception) { null }
                                if (eq != null) {
                                    equipoId = eq.id
                                    referencia = eq.referencia ?: ""
                                    tipo = eq.tipo ?: ""
                                    statusMessage = null
                                } else {
                                    equipoId = null
                                    statusMessage = "Equipo no encontrado"
                                }
                                loading = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF234DB5)),
                        shape = RoundedCornerShape(25.dp),
                        enabled = !loading
                    ) {
                        Text("Buscar", fontSize = 16.sp, color = Color.White)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                if (equipoId == null) {
                                    snackbarHostState.showSnackbar("Busca primero el equipo.")
                                    return@launch
                                }
                                loading = true
                                val result = repo.marcarComoDisponible(equipoId!!)
                                loading = false
                                if (result.isSuccess) {
                                    snackbarHostState.showSnackbar("✅ Equipo devuelto con éxito")
                                    // Limpiar campos
                                    serial = ""
                                    referencia = ""
                                    tipo = ""
                                    equipoId = null
                                } else {
                                    snackbarHostState.showSnackbar(
                                        "❌ Error: ${result.exceptionOrNull()?.message}"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D77E1)),
                        shape = RoundedCornerShape(25.dp),
                        enabled = !loading
                    ) {
                        Text(
                            "Confirmar devolución",
                            fontSize = 15.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                statusMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E8E)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Volver", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
