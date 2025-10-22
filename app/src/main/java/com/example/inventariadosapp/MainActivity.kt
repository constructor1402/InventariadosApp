package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.example.inventariadosapp.admin.topografo.assign.assignnavgraph.AssignNavGraph
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase antes de usar Firestore
        FirebaseApp.initializeApp(this)

        setContent {
            InventariadosAppTheme {
                // 1️⃣ Crear el NavController
                val navController = rememberNavController()

                // 2️⃣ Pasar el NavController al gráfico de navegación
                AssignNavGraph(navController = navController)
            }
        }
    }
}
