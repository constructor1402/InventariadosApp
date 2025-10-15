package com.example.inventariadosapp.screens.admin.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.admin.obra.ObraViewModel
import com.example.inventariadosapp.admin.obra.components.ObraActionButton
import com.example.inventariadosapp.admin.obra.components.ObraTextField
import com.example.inventariadosapp.ui.theme.Kavoon


@Composable
fun ObrasAdminScreen(navController: NavController) {
    val viewModel: ObraViewModel = viewModel()
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔙 Flecha hacia atrás → vuelve al inicio admin
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

            // 🏗️ Contenido principal: gestión de obras
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 70.dp, bottom = 80.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Obras",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campos
                ObraTextField(
                    value = viewModel.nombreObra,
                    onValueChange = viewModel::updateNombreObra,
                    label = "Nombre de Obra",
                    isError = viewModel.nombreObraError,
                    errorMessage = if (viewModel.nombreObraError) "Escribe el nombre de la obra" else null
                )

                ObraTextField(
                    value = viewModel.ubicacion,
                    onValueChange = viewModel::updateUbicacion,
                    label = "Ubicación",
                    isError = viewModel.ubicacionError,
                    errorMessage = if (viewModel.ubicacionError) "Ingresa la ubicación" else null
                )

                ObraTextField(
                    value = viewModel.clienteNombre.ifBlank { "Cliente asociado" },
                    onValueChange = {},
                    label = "Cliente",
                    isError = false,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ObraActionButton(
                        text = "Guardar",
                        color = Color(0xFF4CAF50),
                        icon = "done",
                        onClick = viewModel::guardarObra
                    )
                    ObraActionButton(
                        text = "Buscar",
                        color = Color(0xFF64B5F6),
                        icon = "search",
                        onClick = { viewModel.buscarObra(viewModel.nombreObra) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ObraActionButton(
                    text = "Eliminar",
                    color = Color(0xFFE57373),
                    icon = "delete",
                    onClick = viewModel::eliminarObra
                )

                // Mensaje de estado
                viewModel.mensajeStatus?.let { msg ->
                    Text(
                        text = msg,
                        color = if (msg.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavGestionBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Surface(
        shadowElevation = 12.dp,
        tonalElevation = 3.dp,
        color = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Obras
            GestionNavItem(
                icon = R.drawable.ic_obras,
                label = "Obras",
                isSelected = currentRoute == "obras_admin",
                onClick = {
                    if (currentRoute != "obras_admin") {
                        navController.navigate("obras_admin") {
                            popUpTo("obras_admin") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )

            // 🔹 Equipos
            GestionNavItem(
                icon = R.drawable.ic_equipment,
                label = "Equipos",
                isSelected = currentRoute == "equipos_admin",
                onClick = {
                    if (currentRoute != "equipos_admin") {
                        navController.navigate("equipos_admin") {
                            popUpTo("obras_admin") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )

            // 🔹 Usuarios
            GestionNavItem(
                icon = R.drawable.ic_users,
                label = "Usuarios",
                isSelected = currentRoute == "usuarios_admin",
                onClick = {
                    if (currentRoute != "usuarios_admin") {
                        navController.navigate("usuarios_admin") {
                            popUpTo("obras_admin") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}



@Composable
fun GestionNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected)
        colorResource(id = R.color.boton_principal).copy(alpha = 0.3f)
    else
        colorResource(id = R.color.boton_principal).copy(alpha = 0.1f)

    val textColor = if (isSelected)
        colorResource(id = R.color.texto_principal)
    else
        Color.DarkGray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            color = backgroundColor,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = colorResource(id = R.color.boton_principal),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontFamily = Kavoon
        )
    }
}




