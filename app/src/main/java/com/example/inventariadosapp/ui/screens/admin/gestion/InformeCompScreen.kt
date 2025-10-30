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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompScreen(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel = viewModel(LocalContext.current as androidx.activity.ComponentActivity)
) {
    val equipos by viewModel.equipos.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState() // 👈 Scroll agregado

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
                    IconButton(onClick = { adminNavController.navigate("informes_admin") }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // 👈 Habilita desplazamiento vertical
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 📊 Tabla centrada con los equipos filtrados
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                viewModel.TablaEquiposFirebase(equipos)
            }

            Spacer(Modifier.height(20.dp))

            // 📄 Botón para generar informe PDF
            Button(
                onClick = {
                    coroutineScope.launch {
                        val userData = viewModel.obtenerUsuarioActual()

                        if (userData == null) {
                            snackbarHostState.showSnackbar("No se encontró un usuario autenticado.")
                            return@launch
                        }

                        val (usuarioId, correoUser) = userData
                        val usuarioNombre = correoUser.substringBefore("@")

                        // Generar el informe con los datos del usuario
                        val filePath = viewModel.generarInformePDFequipos(
                            equipos = equipos,
                            usuarioNombre = usuarioNombre,
                            usuarioCorreo = correoUser
                        )

                        if (!filePath.startsWith("Error")) {
                            // Subir el informe a Firebase
                            viewModel.guardarInformeEnFirebase(usuarioId, filePath, "equipos")
                            snackbarHostState.showSnackbar("Informe generado y subido correctamente.")
                        } else {
                            snackbarHostState.showSnackbar(filePath)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_informes),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Generar informe", color = Color.White, fontFamily = Kavoon)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
