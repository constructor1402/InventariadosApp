package com.example.inventariadosapp.admin.obra.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String? = null,
    readOnly: Boolean = false
) {
    val borderColor = if (isError) Color.Red else Color(0xFF7986CB) // Azul suave
    val backgroundColor = Color(0xFFDDE6FF) // Fondo azul claro

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif, // Puedes cambiar por Kavoon si lo tienes importado
                    color = Color.Black
                )
            )
        },
        readOnly = readOnly,
        isError = isError,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily.Serif, // Cambia por Kavoon si la tienes
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )

    // Mensaje de error debajo
    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
        )
    }
}
