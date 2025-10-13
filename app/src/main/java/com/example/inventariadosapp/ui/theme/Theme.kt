package com.example.inventariadosapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Paleta de colores para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Paleta de colores para modo claro
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

@Composable
fun InventariadosAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta si el usuario tiene modo oscuro
    dynamicColor: Boolean = true, // Usa Material You (Android 12+)
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // En Android 12+ usa colores dinámicos del sistema (Material You)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // En versiones anteriores usa los esquemas definidos arriba
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, // Aplica el esquema dinámico o predeterminado
        typography = AppTypography,
        content = content
    )
}

