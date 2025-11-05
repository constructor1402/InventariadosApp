package com.example.inventariadosapp.ui.screens.admin

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserUiState
import com.example.inventariadosapp.ui.screens.admin.informes.InformeEquiposViewModel
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch
import androidx.core.net.toUri
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompUsers(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel,
    userCorreo : String
) {
    val informesUsuario by viewModel.informesUsuario.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var usuarioSeleccionado by remember { mutableStateOf<UserUiState?>(null) }
    val context = LocalContext.current // ✅ Contexto guardado correctamente

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Informes por Usuario",
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Usuario: ${usuarioSeleccionado?.nombre} (${usuarioSeleccionado?.correo})",
                fontFamily = Kavoon,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            if (informesUsuario.isEmpty()) {
                Text("No hay informes generados.", color = Color.Gray)
            } else {
                informesUsuario.forEach { inf ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val url = inf["url"].toString()
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent) // ✅ Usar el context guardado
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(inf["tipo"].toString(), modifier = Modifier.weight(1f))
                        Text(inf["fecha"].toString(), modifier = Modifier.weight(1f))
                        Text("Abrir", color = Color(0xFF1E3A8A), modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val pdfPath = viewModel.generarInformePDFinformes(
                            informesUsuario,
                            usuarioSeleccionado?.correo ?: ""
                        )
                        viewModel.guardarInformeEnFirebase(
                            pdfPath,
                            "Resumen de informes",
                            usuarioSeleccionado?.correo ?: ""
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Generar Informe", color = Color.White, fontFamily = Kavoon)
            }
        }
    }
}

