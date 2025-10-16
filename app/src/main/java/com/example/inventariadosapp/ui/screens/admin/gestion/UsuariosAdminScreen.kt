package com.example.inventariadosapp.screens.admin.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.UserViewModel
import com.example.inventariadosapp.ui.screens.admin.gestion.components.UserActionButtons
import com.example.inventariadosapp.ui.screens.admin.gestion.components.UserForm
import com.example.inventariadosapp.ui.theme.Kavoon
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosAdminScreen(navController: NavController, viewModel: UserViewModel = viewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Colores según tema del sistema
    val backgroundColor = if (isSystemInDarkTheme()) {
        Color(0xFF000000)
    } else {
        colorResource(id = R.color.fondo_claro)
    }

    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color(0xFF3D3D3D),
                    contentColor = Color.White,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(data.visuals.message, textAlign = TextAlign.Center)
                }
            }
        },
        containerColor = backgroundColor
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {

            // 🔙 Botón volver
            IconButton(
                onClick = { navController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            // 🔹 Contenido principal (scrollable)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 70.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val titleColor = if (isSystemInDarkTheme()) Color.White else Color.Black

                Text(
                    text = "Gestión de Usuarios",
                    color = titleColor,
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // 🔸 Formulario
                val state = viewModel.uiState.collectAsState().value
                UserForm(state = state, viewModel = viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                // 🔸 Botones de acción
                UserActionButtons(viewModel = viewModel)

                // 🔸 Mensajes de estado
                LaunchedEffect(viewModel.mensajeEstado) {
                    viewModel.mensajeEstado?.let {
                        scope.launch {
                            snackbarHostState.showSnackbar(it)
                            viewModel.mensajeEstado = null
                        }
                    }
                }
            }
        }
    }
}
