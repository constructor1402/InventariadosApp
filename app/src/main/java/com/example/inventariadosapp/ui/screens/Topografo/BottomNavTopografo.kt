package com.example.inventariadosapp.ui.screens.Topografo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun BottomNavTopografo(
    navController: NavController,
    currentRoute: String
) {
    val bgColor = Color(0xFFF5F5F5)
    val textColor = Color.Black

    Surface(
        color = bgColor,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp,
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
            BottomNavItemTopografo(
                icon = R.drawable.ic_home,
                label = "Inicio",
                isSelected = currentRoute == "inicio_topografo",
                onClick = { if (currentRoute != "inicio_topografo") navController.navigate("inicio_topografo") },
                textColor = textColor
            )
            BottomNavItemTopografo(
                icon = R.drawable.ic_gestion,
                label = "Gestión",
                isSelected = currentRoute == "gestion_topografo",
                onClick = { if (currentRoute != "gestion_topografo") navController.navigate("gestion_topografo") },
                textColor = textColor
            )
            BottomNavItemTopografo(
                icon = R.drawable.ic_informes,
                label = "Informes",
                isSelected = currentRoute == "informes_topografo",
                onClick = { if (currentRoute != "informes_topografo") navController.navigate("informes_topografo") },
                textColor = textColor
            )
        }
    }
}

@Composable
fun BottomNavItemTopografo(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    textColor: Color
) {
    val backgroundColor = if (isSelected)
        colorResource(id = R.color.boton_principal).copy(alpha = 0.3f)
    else
        colorResource(id = R.color.boton_principal).copy(alpha = 0.1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
            fontFamily = Kavoon,
            textAlign = TextAlign.Center
        )
    }
}
