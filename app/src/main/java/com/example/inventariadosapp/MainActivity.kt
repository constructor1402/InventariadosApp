package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventariadosapp.admin.obra.GestionObraScreen
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.google.firebase.FirebaseApp  // 👈 importa FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 👇 Inicializa Firebase antes de usar Firestore
        FirebaseApp.initializeApp(this)

        setContent {
            // Aplica el tema general de la app
            InventariadosAppTheme {
                // Pantalla de prueba para el módulo de obras
                GestionObraScreen()
            }
        }
    }
}

