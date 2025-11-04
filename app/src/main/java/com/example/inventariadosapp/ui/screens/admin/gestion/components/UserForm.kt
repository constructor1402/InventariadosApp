package com.example.inventariadosapp.ui.screens.admin.gestion.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserViewModel
import com.example.inventariadosapp.ui.theme.Kavoon
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserForm(viewModel: UserViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("Admin", "Topógrafo", "Consulta")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nombre completo
        Text(
            text = "Nombre completo",
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.onFieldChange(nombre = it) },
            placeholder = { Text("Ej. Alan Smith", fontFamily = Kavoon) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedIndicatorColor = colorResource(id = R.color.texto_principal),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Celular
        Text(
            text = "Número de celular",
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = viewModel.celular,
            onValueChange = { viewModel.onFieldChange(celular = it) },
            placeholder = { Text("Ej. 3224445555", fontFamily = Kavoon) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedIndicatorColor = colorResource(id = R.color.texto_principal),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Correo electrónico
        Text(
            text = "Correo electrónico",
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
// Correo electrónico
        OutlinedTextField(
            value = viewModel.correo,
            onValueChange = { viewModel.onFieldChange(correo = it) },
            placeholder = { Text("Ej. na@gmail.com", fontFamily = Kavoon) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(52.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedIndicatorColor = colorResource(id = R.color.texto_principal),
                unfocusedIndicatorColor = Color.Gray
            )
        )


        Spacer(modifier = Modifier.height(10.dp))

        // Contraseña
        Text(
            text = "Contraseña",
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = viewModel.contrasena,
            onValueChange = { viewModel.onFieldChange(contrasena = it) },
            placeholder = { Text("********", fontFamily = Kavoon) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Kavoon,
                textAlign = TextAlign.Center
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible)
                    painterResource(id = R.drawable.ic_visibility_off)
                else painterResource(id = R.drawable.ic_visibility)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = icon,
                        contentDescription = "Mostrar u ocultar contraseña",
                        tint = Color.Black
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.boton_principal),
                unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                focusedIndicatorColor = colorResource(id = R.color.texto_principal),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Rol
        Text(
            text = "Rol",
            color = colorResource(id = R.color.texto_principal),
            fontFamily = Kavoon,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            OutlinedTextField(
                value = viewModel.rol.ifBlank { "Seleccionar rol" },
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.boton_principal),
                    unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                    focusedIndicatorColor = colorResource(id = R.color.texto_principal),
                    unfocusedIndicatorColor = Color.Gray
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Kavoon,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(52.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { rol ->
                    DropdownMenuItem(
                        text = { Text(rol, fontFamily = Kavoon) },
                        onClick = {
                            viewModel.onFieldChange(rol = rol)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

