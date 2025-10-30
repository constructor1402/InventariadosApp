package com.example.inventariadosapp.ui.screens.admin



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.screens.admin.gestion.Obra
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompObraScreen(
    adminNavController: NavController,
    obrasFiltradas: List<Obra>,
    viewModel: InformeEquiposViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Informe de equipos",
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
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
        containerColor = Color(0xFFF4F6FB)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Resultados de obras filtradas",
                fontFamily = Kavoon,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF8D8EB5),
                fontSize = 15.sp
            )

            viewModel.TablaObrasFirebase(obrasFiltradas)

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botón generar informe
            Button(
                onClick = {
                    coroutineScope.launch {
                        val usuarioActual = viewModel.obtenerUsuarioActual()
                        if (usuarioActual != null) {
                            val (uid, correo) = usuarioActual
                            val nombre = correo.substringBefore("@")
                            val path = viewModel.generarInformePDFobras(obrasFiltradas, nombre, correo)
                            viewModel.guardarInformeEnFirebase(uid, path, "Informe de Obras")
                            snackbarHostState.showSnackbar("Informe generado y subido correctamente.")
                        } else {
                            snackbarHostState.showSnackbar("No se encontró un usuario autenticado.")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_informes),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Generar informe",
                    color = Color.White,
                    fontFamily = Kavoon
                )
            }
        }
    }
}

