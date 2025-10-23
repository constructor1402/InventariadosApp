package com.example.inventariadosapp.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun InformesAdminScreen(adminNavController: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // 🔙 Flecha hacia atrás que vuelve al inicio
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
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔹 Botón 1: Informe de Equipos
                BotonInforme(
                    texto = "Informe de equipos",
                    colorFondo = Color(0xFF3949AB),
                    onClick = { adminNavController.navigate("informe_equipos") }
                )

                // 🔹 Botón 2: Informe de Obras
                BotonInforme(
                    texto = "Informe de obras",
                    colorFondo = Color(0xFF7B1FA2),
                    onClick = { adminNavController.navigate("informeObras") }
                )

                // 🔹 Botón 3: Informe de Usuarios
                BotonInforme(
                    texto = "Informe de usuarios",
                    colorFondo = Color(0xFF6686E8),
                    onClick = { adminNavController.navigate("informeUsuarios") }
                )
            }
        }
    }
}
@Composable
fun BotonInforme(
    texto: String,
    colorFondo: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(colorFondo),
        modifier = Modifier
            .width(250.dp)
            .height(180.dp)
            .padding(vertical = 20.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = Kavoon,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
