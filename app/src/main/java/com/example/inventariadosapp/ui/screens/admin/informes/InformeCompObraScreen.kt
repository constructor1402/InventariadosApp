package com.example.inventariadosapp.ui.screens.admin



import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.domain.model.Obra
import com.example.inventariadosapp.ui.screens.admin.informes.viewmodels.InformeEquiposViewModel
import com.example.inventariadosapp.ui.screens.admin.informes.viewmodels.InformeObrasViewModel
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompObraScreen(
    adminNavController: NavController,
    obrasFiltradas: List<Obra>,
    viewModel: InformeEquiposViewModel,
    userCorreo: String
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

            TablaObrasFirebase(obrasFiltradas)

            Spacer(modifier = Modifier.height(20.dp))

            val viewModel: InformeObrasViewModel = viewModel()

            // 🔹 Botón generar informe
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (userCorreo.isBlank()) {
                            snackbarHostState.showSnackbar("Error: usuario no identificado.")
                            return@launch
                        }

                        val filePath = viewModel.generarInformePDFobras(
                            obras = obrasFiltradas,
                            userCorreo = userCorreo
                        )

                        if (!filePath.startsWith("Error")) {
                            viewModel.guardarInformeEnFirebase(filePath, tipo = "obras", userCorreo)
                            snackbarHostState.showSnackbar("Informe generado y subido.")
                        } else {
                            snackbarHostState.showSnackbar(filePath)
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
@Composable
fun TablaObrasFirebase(obras: List<Obra>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 🧭 Encabezado de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6686E8)) // azul suave
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("ID Obra", "Nombre Obra", "Ubicación", "Cliente").forEach {
                Text(
                    text = it,
                    color = Color.White,
                    fontFamily = Kavoon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 📋 Filas de datos
        obras.forEachIndexed { index, eq ->
            val fondoFila = if (index % 2 == 0) Color(0xFFEFF1F9) else Color.White

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondoFila)
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = eq.idObra,
                    fontFamily = Kavoon,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = eq.nombreObra,
                    fontFamily = Kavoon,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = eq.ubicacion,
                    fontFamily = Kavoon,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = eq.clienteNombre,
                    fontFamily = Kavoon,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}


