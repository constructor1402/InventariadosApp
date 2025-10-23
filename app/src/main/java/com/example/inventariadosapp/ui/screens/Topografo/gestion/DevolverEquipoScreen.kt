package com.example.inventariadosapp.ui.screens.Topografo.gestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment //  👈 --- IMPORTACIÓN AÑADIDA ---
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
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun DevolverEquipoScreen(
    serialArg: String,
    navController: NavController,
    onScanClick: () -> Unit,
    onManualClick: () -> Unit,
    onConfirmarDevolucion: () -> Unit
) {
    var mostrarFormulario by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavGestionTopografo(navController = navController, currentRoute = "devolver_equipo")
        },
        containerColor = Color(0xFFD7E2FF)
    ) { padding ->

        //  👇 --- BOX AÑADIDO (PARA LA FLECHA) ---
        // Este Box envuelve todo el contenido y permite poner la flecha encima
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Aplicamos el padding del Scaffold al Box
        ) {
            //  👇 --- FLECHA AÑADIDA (Igual a la de Asignar) ---
            IconButton(
                onClick = { navController.navigate("inicio_topografo") }, // Navega al inicio
                modifier = Modifier
                    .align(Alignment.TopStart) // La alinea arriba a la izquierda
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            // --- Tu código original (if/else) va aquí dentro ---
            if (!mostrarFormulario) {
                // 🟢 Primera vista: selección del método (igual Figma)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        // Quitamos el padding del scaffold de aquí
                        .padding(horizontal = 24.dp)
                        // Añadimos padding vertical para centrar y no chocar con la flecha
                        .padding(vertical = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Devolver Equipo",
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.texto_principal),
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )

                    // Escanear con cámara
                    Button(
                        onClick = {
                            onScanClick()
                            mostrarFormulario = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde_admin)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(120.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Escanear con cámara",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Escanear\ncon cámara",
                                fontFamily = Kavoon,
                                color = Color.White,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // Ingresar manual
                    Button(
                        onClick = {
                            onManualClick()
                            mostrarFormulario = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.azul_admin)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(120.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "Ingresar manualmente",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ingresar\nDatos Manual",
                                fontFamily = Kavoon,
                                color = Color.White,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // 🟣 Segunda vista: formulario después del escaneo o ingreso
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        // Añadimos padding vertical para centrar y no chocar con la flecha
                        .padding(vertical = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Datos del Equipo",
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.texto_principal),
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    CampoLectura(label = "Serial", value = serialArg)
                    CampoLectura(label = "Referencia", value = "Nivel Topográfico")
                    CampoLectura(label = "Tipo de Equipo", value = "Herramienta")
                    CampoLectura(label = "Estado", value = "Asignado")
                    CampoLectura(label = "Obra Actual", value = "Puente Soacha")

                    Spacer(modifier = Modifier.height(35.dp))

                    Button(
                        onClick = { onConfirmarDevolucion() },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde_admin)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(65.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_devolver),
                            contentDescription = "Confirmar devolución",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Confirmar Devolución",
                            color = Color.White,
                            fontFamily = Kavoon,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        } // --- FIN DEL BOX AÑADIDO ---
    }
}

@Composable
fun CampoLectura(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth(0.9f)) {
        Text(
            text = label,
            fontFamily = Kavoon,
            color = Color(0xFF1E3A8A),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value.ifEmpty { "—" },
                color = Color(0xFF111111),
                fontSize = 16.sp,
                fontFamily = Kavoon
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}