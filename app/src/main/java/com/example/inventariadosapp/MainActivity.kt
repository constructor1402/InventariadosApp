package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.example.inventariadosapp.admin.topografo.assign.AssignNavGraph.AssignNavGraph
// ** NUEVA IMPORTACIÓN NECESARIA **
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase antes de usar Firestore
        FirebaseApp.initializeApp(this)

        setContent {
            InventariadosAppTheme {
                // 1. CREAR EL NAV CONTROLLER
                val navController = rememberNavController()

                // 2. PASAR EL NAV CONTROLLER A LA FUNCIÓN
                AssignNavGraph(navController = navController)
            }
        }
    }
}