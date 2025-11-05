package com.example.inventariadosapp.ui.screens.admin.informes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserUiState
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeUsuariosScreen(
    adminNavController: NavController,
    userCorreo: String,
    viewModel: InformeEquiposViewModel = viewModel()
) {
    var usuarioBusqueda by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Aquí obtenemos la lista de usuarios de forma reactiva
    val usuarios by viewModel.users.collectAsState(initial = emptyList()) // usar observeAsState() si es LiveData
    var expanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserUiState?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.fondo_claro)),
                title = {
                    Text(
                        "Informe de Usuarios",
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { adminNavController.navigate("informes_admin/$userCorreo") },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
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
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.fondo_claro))
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_user),
                contentDescription = "Usuarios",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(90.dp)
                    .padding(top = 8.dp)
            )

            Text(
                "Nombre de Usuario",
                fontFamily = Kavoon,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextField(
                value = usuarioBusqueda,
                onValueChange = { usuarioBusqueda = it },
                placeholder = {
                    Text(
                        "Digite el nombre de usuario",
                        fontFamily = Kavoon,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF8D8EB5),
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.campo_fondo),
                    unfocusedContainerColor = colorResource(id = R.color.campo_fondo),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Text(
                "Correo del Usuario",
                fontFamily = Kavoon,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextField(
                value = correoUsuario,
                onValueChange = { correoUsuario = it },
                placeholder = {
                    Text(
                        "Digite el correo del usuario",
                        fontFamily = Kavoon,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF8D8EB5),
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.campo_fondo),
                    unfocusedContainerColor = colorResource(id = R.color.campo_fondo),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = {
                    scope.launch {
                        viewModel.buscarUsuarios(usuarioBusqueda, correoUsuario)
                        if (usuarios.size == 1) {
                            val correo = usuarios.first().correo
                            viewModel.cargarInformesUsuario(correo)
                            adminNavController.navigate("resultados_users/$correo")
                        } else {
                            expanded = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.azul_admin)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(55.dp)
                    .padding(top = 12.dp)
            ) {
                Text(
                    "Buscar",
                    fontFamily = Kavoon,
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }

            // DropDownMenu para seleccionar usuario si hay más de uno
            if (usuarios.size > 1) {
                Text("Selecciona un usuario:")
                Box {
                    Button(onClick = { expanded = true }) {
                        Text(selectedUser?.nombre ?: "Elegir usuario")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        usuarios.forEach { usuario ->
                            DropdownMenuItem(
                                text = { Text(usuario.nombre) },
                                onClick = {
                                    expanded = false
                                    selectedUser = usuario
                                    scope.launch {
                                        viewModel.cargarInformesUsuario(usuario.correo)
                                        adminNavController.navigate("resultados_users/${usuario.correo}")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
