package com.example.inventariadosapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserUiState
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCompUsers(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel
) {
    val users by viewModel.users.collectAsState()
    val informesUsuario by viewModel.informesUsuario.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var usuarioSeleccionado by remember { mutableStateOf<UserUiState?>(null) }

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
                    IconButton(onClick = { adminNavController.navigate("informes_admin") }) {
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
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            if (users.isEmpty()) {
                Text(
                    "No se encontraron usuarios.",
                    fontFamily = Kavoon,
                    color = Color(0xFF8D8EB5),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                users.forEach { usuario ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "👤 ${usuario.nombre} (${usuario.correo})",
                            fontFamily = Kavoon,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF1E3A8A),
                            fontSize = 17.sp
                        )

                        Button(
                            onClick = {
                                usuarioSeleccionado = usuario
                                coroutineScope.launch {
                                    viewModel.cargarInformesUsuario(usuario.correo)
                                }
                            },
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ver informes", color = Color.White, fontFamily = Kavoon)
                        }

                        if (usuarioSeleccionado == usuario) {
                            Spacer(modifier = Modifier.height(10.dp))
                            if (informesUsuario.isEmpty()) {
                                Text(
                                    "Este usuario no tiene informes generados.",
                                    fontFamily = Kavoon,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            } else {
                                viewModel.TablaInformesUsuario(informesUsuario)
                            }
                        }

                        Divider(
                            color = Color(0xFFD1D5DB),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
