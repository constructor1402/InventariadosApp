package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportNavGraph
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializar Firebase antes de cualquier acceso a Firestore o Auth
        FirebaseApp.initializeApp(this)

        setContent {
            InventariadosAppTheme {
                Surface(color = Color.White) {
                    // ✅ Controlador de navegación principal
                    val navController = rememberNavController()

                    // ✅ Llamamos al NavGraph de Reportes
                    ReportNavGraph(navController = navController)
                }
            }
        }
    }
}
