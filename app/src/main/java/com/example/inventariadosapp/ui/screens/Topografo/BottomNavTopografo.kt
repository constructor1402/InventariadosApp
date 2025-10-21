package com.example.inventariadosapp.ui.screens.Topografo


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.inventariadosapp.R

@Composable
fun BottomNavTopografo(navController: NavController) {
    NavigationBar(
        containerColor = colorResource(id = R.color.boton_principal)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("inicio_topografo") },
            icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("gestion_topografo") },
            icon = { Icon(painterResource(id = R.drawable.ic_gestion), contentDescription = "Gestión") },
            label = { Text("Gestión") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("informes_topografo") },
            icon = { Icon(painterResource(id = R.drawable.ic_informes), contentDescription = "Informes") },
            label = { Text("Informes") }
        )
    }
}

