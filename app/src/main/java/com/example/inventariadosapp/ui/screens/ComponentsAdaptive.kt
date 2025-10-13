package com.example.inventariadosapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun TextLabelAdaptive(text: String) {
    androidx.compose.material3.Text(
        text = text,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = Kavoon,
            fontSize = 16.sp
        ),
        modifier = Modifier
            .padding(bottom = 4.dp, top = 12.dp)
            .fillMaxWidth()
    )
}

@Composable
fun InputFieldAdaptive(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            androidx.compose.material3.Text(
                text = placeholder,
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.Black.copy(alpha = 0.5f),
                fontFamily = Kavoon,
                fontSize = 15.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.boton_principal),
            unfocusedContainerColor = colorResource(id = R.color.boton_principal),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontFamily = Kavoon,
            fontSize = 16.sp
        )
    )
}
