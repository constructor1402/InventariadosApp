package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventariadosapp.admin.obra.GestionObraScreen
import com.example.inventariadosapp.navigation.AppNavigation
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserManagementScreen
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa Firebase solo si aún no está inicializado
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        setContent {
            // Aplica el tema general de la app
            InventariadosAppTheme {
                // 🔹 Navegación principal
                AppNavigation()

                // 🔹 Módulo de prueba de obras (si se quiere mantener visible para test)
                // GestionObraScreen()
            }
        }
    }
}
