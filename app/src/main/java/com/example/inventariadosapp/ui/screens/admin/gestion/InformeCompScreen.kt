package com.example.inventariadosapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompScreen(
    adminNavController: NavController,
    userCorreo: String,
    viewModel: InformeEquiposViewModel = viewModel(LocalContext.current as androidx.activity.ComponentActivity)
) {
    val equipos by viewModel.equipos.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.fondo_claro)),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.fondo_claro)),
                title = {
                    Text(
                        text = "Informe de equipos",
                        fontFamily = Kavoon,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { adminNavController.navigate("informes_admin/$userCorreo") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 📜 Contenido desplazable centrado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 90.dp), // deja espacio para el botón
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TablaEquiposFirebase(equipos)
                }
            }

            // 📄 Botón fijo en la parte inferior
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (userCorreo.isBlank()) {
                            snackbarHostState.showSnackbar("No se encontró el Correo del usuario.")
                            return@launch
                        }

                        val usuarioCorreo = userCorreo

                        val filePath = viewModel.generarInformePDFequipos(
                            equipos = equipos,
                            usuarioCorreo = usuarioCorreo
                        )

                        if (!filePath.startsWith("Error")) {
                            viewModel.guardarInformeEnFirebase(filePath, "equipos", userCorreo)
                            snackbarHostState.showSnackbar("Informe generado y subido correctamente.")
                        } else {
                            snackbarHostState.showSnackbar(filePath)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_informes),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Generar informe", color = Color.White, fontFamily = Kavoon)
            }
        }
    }
}

@Composable
fun TablaEquiposFirebase(equipos: List<Equipo>) {
    val encabezadoColor = Color(0xFF6686E8)
    val filaPar = Color(0xFFF7F8FC)
    val filaImpar = Color(0xFFFFFFFF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        // 🔹 Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(encabezadoColor)
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Serial", "Referencia", "Tipo", "Fecha").forEach {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Kavoon,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 🔹 Filas con colores alternados
        equipos.forEachIndexed { index, eq ->
            val fondo = if (index % 2 == 0) filaPar else filaImpar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondo)
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(eq.serial, modifier = Modifier.weight(1f), fontSize = 12.sp)
                Text(eq.referencia, modifier = Modifier.weight(1f), fontSize = 12.sp)
                Text(eq.tipo, modifier = Modifier.weight(1f), fontSize = 12.sp)
                Text(eq.fechaCertificacion, modifier = Modifier.weight(1f), fontSize = 12.sp)
            }
        }
    }
}

