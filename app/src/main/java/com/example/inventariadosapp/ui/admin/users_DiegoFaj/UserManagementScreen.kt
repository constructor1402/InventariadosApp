package com.example.inventariadosapp.ui.admin.users_DiegoFaj

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.components.UserActionButtons
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.components.UserForm
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(viewModel: UserViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val backgroundColor = if (isSystemInDarkTheme()) {
        Color(0xFF000000) // Modo oscuro → negro
    } else {
        Color(0xFFDDE6FF) // Modo claro → azul pastel claro
    }


    Scaffold(
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



        // 🔸 Título principal
            val titleColor = if (isSystemInDarkTheme()) Color.White else Color.Black

            Text(
                text = "Gestión de Usuarios",
                color = titleColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )


            // 🔹 Formulario
            val state = viewModel.uiState.collectAsState().value
            UserForm(state = state, viewModel = viewModel)


            // 🔹 Botones de acción
            Spacer(modifier = Modifier.height(16.dp))
            UserActionButtons(viewModel = viewModel)

            // 🔹 Mostrar mensajes de estado
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserManagementScreen() {

    Surface(modifier = Modifier.fillMaxSize()) {
        UserManagementScreen()
    }
}



