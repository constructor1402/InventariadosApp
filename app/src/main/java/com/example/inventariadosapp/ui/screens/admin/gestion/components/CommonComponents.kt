package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

// 🔹 Campo de texto reutilizable (idéntico para Obras y Equipos)
@Composable
fun CustomTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = Kavoon,
                    textAlign = TextAlign.Center
                )
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEDEDED),
                unfocusedContainerColor = Color(0xFFEDEDED),
                focusedIndicatorColor = colorResource(id = R.color.boton_principal),
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = colorResource(id = R.color.boton_principal)
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(52.dp)
        )
    }
}

// 🔹 Botón de acción reutilizable (Guardar, Buscar, Eliminar)
@Composable
fun ActionButton(text: String, color: Color, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(156.dp)
            .height(45.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = Kavoon,
                modifier = Modifier.padding(end = 6.dp)
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

