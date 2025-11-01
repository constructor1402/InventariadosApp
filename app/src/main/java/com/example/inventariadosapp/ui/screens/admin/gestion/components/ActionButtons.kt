package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun ActionButtons(
    onGuardar: (() -> Unit)? = null,
    onBuscar: (() -> Unit)? = null,
    onEliminar: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // 💾 Botón Guardar (solo si se usa)
        if (onGuardar != null) {
            Button(
                onClick = onGuardar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(55.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = "Guardar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Guardar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon
                )
            }
        }

        // 🔍 Botón Buscar (solo si se usa)
        if (onBuscar != null) {
            Button(
                onClick = onBuscar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(55.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Buscar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buscar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon
                )
            }
        }

        // 🗑️ Botón Eliminar (solo si se usa)
        if (onEliminar != null) {
            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(55.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Eliminar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon
                )
            }
        }
    }
}


