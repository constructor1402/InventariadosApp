package com.example.inventariadosapp.ui.screens.Topografo.assign.models

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.admin.topografo.assign.components.CameraPreview
import com.example.inventariadosapp.ui.screens.Topografo.assign.AssignNavGraph.AssignRoutes
import com.example.inventariadosapp.ui.theme.Kavoon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignCameraScreen(navController: NavHostController, viewModel: TopografoAssignViewModel) {

    var scannedSerial by remember { mutableStateOf<String?>(null) }
    val isLoading = viewModel.isLoading

    val backgroundColor = colorResource(id = R.color.fondo_claro)
    val textColor = colorResource(id = R.color.texto_principal)
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_roja),
                            contentDescription = "Cerrar",
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->

        if (scannedSerial == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Apunta la cámara al serial del equipo",
                    fontFamily = Kavoon,
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                ) {
                    CameraPreview(
                        onTextFound = { serial ->
                            scannedSerial = serial
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal),
                        contentColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        fontFamily = Kavoon,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // 🔹 ALERT DIALOG CORREGIDO
        if (scannedSerial != null) {
            AlertDialog(
                onDismissRequest = {
                    if (!isLoading) {
                        scannedSerial = null
                    }
                },
                title = { Text("Equipo detectado") },
                text = {
                    Column {
                        Text("Serial: $scannedSerial")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("¿Deseas confirmar este equipo?")

                        if (isLoading) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // ✅ Esperar a que Firebase cargue los datos ANTES de navegar
                            viewModel.fetchEquipoData(scannedSerial!!) { success ->
                                if (success) {
                                    Toast.makeText(context, "Datos cargados", Toast.LENGTH_SHORT).show()
                                    navController.navigate(AssignRoutes.MANUAL)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "No se encontró el equipo con serial $scannedSerial",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    scannedSerial = null
                                }
                            }
                        },
                        enabled = !isLoading
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            scannedSerial = null
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Escanear de nuevo")
                    }
                }
            )
        }
    }
}
