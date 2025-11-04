package com.example.inventariadosapp.ui.screens.admin.informes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeUsuariosScreen(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel = viewModel()
){
    var usuarioBusqueda by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
                        onClick = { adminNavController.navigate("informes_admin") },
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
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)

        ){
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
                onValueChange = {usuarioBusqueda = it},
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
                onValueChange = {correoUsuario = it},
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
                            viewModel.buscarUsuarios(usuarioBusqueda,correoUsuario)
                            adminNavController.navigate("resultados_users")
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
        }

    }
}