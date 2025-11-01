package com.example.inventariadosapp.screens.admin.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.inventariadosapp.ui.screens.admin.gestion.components.CustomTextField
import com.example.inventariadosapp.ui.theme.Kavoon


@Composable
fun ObrasAdminScreen(navController: NavController) {
    val viewModel: ObraViewModel = viewModel() // 🔗 Conexión al ViewModel
    val scrollState = rememberScrollState()
    val darkTheme = isSystemInDarkTheme()

    Scaffold(
        bottomBar = { BottomNavGestionBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 90.dp, bottom = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Obras",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campos conectados al ViewModel
                CustomTextField(
                    label = "Nombre de Obra",
                    placeholder = "Escribe nombre de la obra",
                    value = viewModel.nombreObra,
                    onValueChange = { viewModel.updateNombreObra(it) }
                )

                CustomTextField(
                    label = "Ubicación",
                    placeholder = "Ingresa ubicación",
                    value = viewModel.ubicacion,
                    onValueChange = { viewModel.updateUbicacion(it) }
                )

                CustomTextField(
                    label = "Cliente",
                    placeholder = "Cliente asociado",
                    value = viewModel.clienteNombre,
                    onValueChange = { viewModel.updateCliente(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButtons(
                        onGuardar = { viewModel.guardarObra() },
                        onBuscar = { viewModel.buscarObra() },
                        onEliminar = { viewModel.eliminarObra() }
                    )

                }

                // Mensaje dinámico
                viewModel.mensaje.let { mensaje ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = mensaje,
                        color = when {
                            mensaje.startsWith("✅") -> Color(0xFF2E7D32)
                            mensaje.startsWith("⚠️") -> Color(0xFFFFA000)
                            else -> Color(0xFFD32F2F)
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }

            // 🔙 Flecha hacia atrás (con zIndex para estar arriba del contenido)
            IconButton(
                onClick = { navController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
                    .zIndex(2f) // 👈 asegura que esté por encima de todo
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver al inicio",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

    }
}


// 🔹 Barra de navegación inferior (sin cambios)
@Composable
fun BottomNavGestionBar(navController: NavController) {
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color(0xFF2C2C2C) else Color(0xFFF5F5F5)
    val textColor = if (darkTheme) Color.White else Color.Black

    Surface(
        shadowElevation = 12.dp,
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GestionNavItem(R.drawable.ic_obras, "Obras", navController, "obras_admin", textColor)
            GestionNavItem(R.drawable.ic_equipment, "Equipos", navController, "equipos_admin", textColor)
            GestionNavItem(R.drawable.ic_users, "Usuarios", navController, "usuarios_admin", textColor)
        }
    }
}

// 🔹 Ítem de navegación
@Composable
fun GestionNavItem(icon: Int, label: String, navController: NavController, route: String, textColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { navController.navigate(route) }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = colorResource(id = R.color.boton_principal),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontFamily = Kavoon
        )
    }
}
