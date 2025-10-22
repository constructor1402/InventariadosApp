package com.example.inventariadosapp.admin.topografo.assign.components.models

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.admin.topografo.assign.components.CameraPreview

@Composable
fun AssignCameraScreen(
    viewModel: TopografoAssignViewModel = viewModel()
) {
    val serial by viewModel.serial.collectAsState()
    val referencia by viewModel.referencia.collectAsState()
    val tipo by viewModel.tipo.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val error by viewModel.error.collectAsState()

    var isScanning by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Solicitud de permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true
            isScanning = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isScanning) "Apunta la cámara al serial del equipo" else "Serial Detectado: $serial",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                !hasCameraPermission -> {
                    Text("Se requiere permiso de cámara para continuar.")
                }

                isScanning -> {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize()
                    ) { scannedValue ->
                        viewModel.updateSerial(scannedValue)
                        viewModel.buscarEquipo(scannedValue)
                        isScanning = false
                    }
                }

                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Referencia: $referencia", style = MaterialTheme.typography.bodyLarge)
                        Text("Tipo: $tipo", style = MaterialTheme.typography.bodyLarge)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.updateSerial("")
                                isScanning = true
                            }
                        ) {
                            Text("Escanear de Nuevo")
                        }
                    }
                }
            }
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::guardarAsignacion,
            enabled = serial.isNotEmpty() && referencia.isNotEmpty()
        ) {
            Text("Guardar Asignación")
        }
    }
}
