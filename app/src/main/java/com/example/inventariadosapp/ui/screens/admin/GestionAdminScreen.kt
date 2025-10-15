package com.example.inventariadosapp.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun GestionAdminScreen(adminNavController: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔙 Flecha hacia atrás que vuelve a InicioAdminScreen
            IconButton(
                onClick = { adminNavController.navigate("inicio_admin") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión",
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    fontSize = 22.sp
                )
            }
        }
    }
}
