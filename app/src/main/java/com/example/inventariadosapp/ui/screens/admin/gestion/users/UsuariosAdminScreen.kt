package com.example.inventariadosapp.ui.screens.admin.gestion.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.components.ActionButtons
import com.example.inventariadosapp.ui.screens.admin.gestion.components.UserForm
import com.example.inventariadosapp.ui.screens.admin.gestion.components.BottomNavGestionBar
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosAdminScreen(navController: NavController, viewModel: UserViewModel = viewModel()) {

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔙 Flecha hacia atrás
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
                    .zIndex(2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver al inicio",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 90.dp, bottom = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Usuarios",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                UserForm(viewModel = viewModel)

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtons(
                    onGuardar = { viewModel.guardarUsuario() },
                    onBuscar = { viewModel.buscarUsuarioPorCorreo() },
                    onEliminar = { viewModel.eliminarUsuario() }
                )


                // 🔔 Muestra snackbar cuando cambia el mensaje
                val mensaje = viewModel.mensajeEstado
                LaunchedEffect(mensaje) {
                    mensaje?.let {
                        if (it.isNotBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(it)
                            }
                        }
                    }
                }

                // 🟢 Mensaje fijo visible también
                viewModel.mensajeEstado?.let { mensaje ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = mensaje,
                        color = when {
                            mensaje.contains("✅") -> Color(0xFF2E7D32)
                            mensaje.contains("🗑️") -> Color(0xFFE53935)
                            mensaje.contains("⚠️") -> Color(0xFFFFA000)
                            else -> Color(0xFFD32F2F)
                        },
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }
        }
    }
}
