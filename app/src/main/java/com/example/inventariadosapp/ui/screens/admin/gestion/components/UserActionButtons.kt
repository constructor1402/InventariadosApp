package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserViewModel
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun UserActionButtons(viewModel: UserViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 💾 Guardar
        StyledActionButton(
            text = "Guardar",
            color = Color(0xFF4CAF50),
            icon = R.drawable.ic_save,
            onClick = { viewModel.guardarUsuario() }
        )

        // 🔍 Buscar
        StyledActionButton(
            text = "Buscar",
            color = Color(0xFF2196F3),
            icon = R.drawable.ic_search,
            onClick = { viewModel.buscarUsuario() }
        )

        // 🗑️ Eliminar
        StyledActionButton(
            text = "Eliminar",
            color = Color(0xFFE53935),
            icon = R.drawable.ic_delete,
            onClick = { viewModel.eliminarUsuario() }
        )
    }
}

@Composable
fun StyledActionButton(text: String, color: Color, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(52.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = Kavoon
        )
    }
}
