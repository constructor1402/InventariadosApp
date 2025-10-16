package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.UserUiState
import com.example.inventariadosapp.ui.screens.admin.gestion.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserForm(state: UserUiState, viewModel: UserViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        //Nombre
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onFieldChange(nombre = it) },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedBorderColor = colorResource(id = R.color.texto_principal),
                unfocusedBorderColor = colorResource(id = R.color.texto_principal)
            )
        )
        //Teléfono
        OutlinedTextField(
            value = state.celular,
            onValueChange = { viewModel.onFieldChange(celular = it) },
            label = { Text("Número de celular") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedBorderColor = colorResource(id = R.color.texto_principal),
                unfocusedBorderColor = colorResource(id = R.color.texto_principal)
            )
        )


        //Correo
        OutlinedTextField(
            value = state.correo,
            onValueChange = { viewModel.onFieldChange(correo = it) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedBorderColor = colorResource(id = R.color.texto_principal),
                unfocusedBorderColor = colorResource(id = R.color.texto_principal)
            )


        )

        //Contraseña
        OutlinedTextField(
            value = state.contrasena,
            onValueChange = { viewModel.onFieldChange(contrasena = it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedBorderColor = colorResource(id = R.color.texto_principal),
                unfocusedBorderColor = colorResource(id = R.color.texto_principal)
            )

        )

        // Selector de Rol
        val roles = listOf("Admin", "Topógrafo", "Consulta")
        var expanded by remember { mutableStateOf(false) }
        val uiState by viewModel.uiState.collectAsState()

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = uiState.rol,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor() // 🔹vincular el menú al campo
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.boton_principal),
                    unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                    focusedBorderColor = colorResource(id = R.color.texto_principal),
                    unfocusedBorderColor = colorResource(id = R.color.texto_principal)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                roles.forEach { rol ->
                    DropdownMenuItem(
                        text = { Text(rol) },
                        onClick = {
                            viewModel.onFieldChange(rol = rol)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }





        if (state.mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.mensaje,
                color = colorResource(id = R.color.texto_principal)
            )
        }
    }
}



