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

        // ✅ Inicializa Firebase antes de cualquier acceso a Firestore o Auth
        FirebaseApp.initializeApp(this)

        setContent {
            InventariadosAppTheme {
                // ✅ Crea el controlador de navegación
                val navController = rememberNavController()

                // ✅ Llama al NavGraph principal del módulo "topógrafo"
                AssignNavGraph(navController = navController)
            }
        }
    }
}
