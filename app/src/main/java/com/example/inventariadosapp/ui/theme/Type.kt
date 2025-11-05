package com.example.inventariadosapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.R

// Fuentes personalizadas
val BungeeInline = FontFamily(Font(R.font.bungeeinline_regular))
val Kavoon = FontFamily(Font(R.font.kavoon_regular))

// Tipografía base que usa los colores del tema activo (claro u oscuro)
val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = BungeeInline,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Kavoon,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Kavoon,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
)


// Función auxiliar para acceder más fácil desde las pantallas
@Composable
fun appTextColor() = MaterialTheme.colorScheme.onBackground

