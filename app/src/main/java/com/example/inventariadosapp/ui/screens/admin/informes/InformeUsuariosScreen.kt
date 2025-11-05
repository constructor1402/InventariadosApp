package com.example.inventariadosapp.ui.screens.admin.informes

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.ui.screens.admin.informes.viewmodels.InformeUsuariosViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeUsuariosScreen(
    navController: NavController,
    userCorreo: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: InformeUsuariosViewModel = viewModel()

    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informe de Usuarios") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.example.inventariadosapp.R.drawable.ic_back),
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Generando informe, por favor espera…")
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                isLoading = true
                                val usuarios = viewModel.obtenerUsuarios()
                                if (usuarios.isEmpty()) {
                                    Toast.makeText(context, "No hay usuarios registrados", Toast.LENGTH_SHORT).show()
                                } else {
                                    val path = viewModel.generarInformePDFusuarios(usuarios, userCorreo)
                                    Toast.makeText(context, "Informe guardado en: $path", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("📄 Generar Informe de Usuarios")
                }
            }
        }
    }
}
