package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventariadosapp.navigation.AppNavigation
import com.example.inventariadosapp.ui.screens.Topografo.gestion.DevolverEquipoNav
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventariadosAppTheme {
                AppNavigation()
            }
        }
    }
}
