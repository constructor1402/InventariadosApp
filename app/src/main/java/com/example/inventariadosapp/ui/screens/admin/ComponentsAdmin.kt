package com.example.inventariadosapp.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
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
            // 🏠 INICIO
            BottomNavItem(
                icon = R.drawable.ic_home,
                label = "Inicio",
                isSelected = currentRoute == "inicio_admin",
                onClick = {
                    if (currentRoute != "inicio_admin") {
                        navController.navigate("inicio_admin") {
                            popUpTo("inicio_admin") { inclusive = false }
                        }
                    }
                }
            )

            // ⚙️ GESTIÓN
            BottomNavItem(
                icon = R.drawable.ic_gestion,
                label = "Gestión",
                isSelected = currentRoute == "gestion_admin",
                onClick = {
                    if (currentRoute != "gestion_admin") {
                        navController.navigate("gestion_admin") {
                            popUpTo("inicio_admin") { inclusive = false }
                        }
                    }
                }
            )

            // 📊 INFORMES
            BottomNavItem(
                icon = R.drawable.ic_informes,
                label = "Informes",
                isSelected = currentRoute == "informes_admin",
                onClick = {
                    if (currentRoute != "informes_admin") {
                        navController.navigate("informes_admin") {
                            popUpTo("inicio_admin") { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) colorResource(id = R.color.boton_principal).copy(alpha = 0.3f)
        else colorResource(id = R.color.boton_principal).copy(alpha = 0.1f)

    val textColor =
        if (isSelected) colorResource(id = R.color.texto_principal)
        else Color.DarkGray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            color = backgroundColor,
            shape = CircleShape,
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
