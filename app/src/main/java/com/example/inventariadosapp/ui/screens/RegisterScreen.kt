package com.example.inventariadosapp.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.animation.animateColorAsState
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.BungeeInline
import com.example.inventariadosapp.ui.theme.Kavoon
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var isLoading by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("") }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // 🔙 Botón para volver
            IconButton(
                onClick = { navController.navigate("bienvenida") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    modifier = Modifier.size(30.dp)
                )
            }

            val scrollState = rememberScrollState()

            // 🔹 Contenido principal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Registro de usuario",
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.texto_principal),
                    fontFamily = Kavoon,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔸 Campos
                TextLabel("Nombre completo")
                InputField(
                    value = nombre,
                    placeholder = "Nombre completo",
                    onValueChange = { nombre = it },
                    keyboardType = KeyboardType.Text
                )

                TextLabel("Número de celular")
                InputField(
                    value = celular,
                    placeholder = "Ejemplo: +573121110000",
                    onValueChange = { celular = it },
                    keyboardType = KeyboardType.Phone
                )

                TextLabel("Correo electrónico")
                InputField(
                    value = correo,
                    placeholder = "Correo electrónico",
                    onValueChange = { correo = it },
                    keyboardType = KeyboardType.Email
                )

                TextLabel("Contraseña")
                InputField(
                    value = contrasena,
                    placeholder = "Contraseña",
                    onValueChange = { contrasena = it },
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                TextLabel("Confirmar contraseña")
                InputField(
                    value = confirmar,
                    placeholder = "Confirmar contraseña",
                    onValueChange = { confirmar = it },
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔸 Rol
                TextLabel("Selecciona un rol")

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    RolButton("Admin", rolSeleccionado == "Admin") { rolSeleccionado = "Admin" }
                    RolButton("Topógrafo", rolSeleccionado == "Topógrafo") { rolSeleccionado = "Topógrafo" }
                    RolButton("Consulta", rolSeleccionado == "Consulta") { rolSeleccionado = "Consulta" }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🔹 BOTÓN REGISTRARSE
                Button(
                    onClick = {
                        if (nombre.isNotBlank() && celular.isNotBlank() && correo.isNotBlank() &&
                            contrasena.isNotBlank() && confirmar.isNotBlank() && rolSeleccionado.isNotBlank()
                        ) {
                            if (contrasena == confirmar) {
                                isLoading = true

                                // 🔹 Verificar si ya existe el usuario con el mismo correo
                                db.collection("usuarios")
                                    .whereEqualTo("correoElectronico", correo)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        if (snapshot.isEmpty) {
                                            // 🔹 Crear usuario en Firestore
                                            val usuario = hashMapOf(
                                                "nombreCompleto" to nombre,
                                                "numeroCelular" to celular,
                                                "correoElectronico" to correo,
                                                "contrasena" to contrasena,
                                                "rolSeleccionado" to rolSeleccionado,
                                                "fechaRegistro" to Timestamp.now()
                                            )

                                            db.collection("usuarios")
                                                .add(usuario)
                                                .addOnSuccessListener {
                                                    isLoading = false
                                                    Toast.makeText(
                                                        context,
                                                        "✅ Usuario registrado correctamente",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    navController.navigate("login")
                                                }
                                                .addOnFailureListener { e ->
                                                    isLoading = false
                                                    Toast.makeText(
                                                        context,
                                                        "❌ Error al guardar datos: ${e.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        } else {
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "⚠️ El correo ya está registrado",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "⚠️ Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "⚠️ Completa todos los campos", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal)
                    ),
                    modifier = Modifier
                        .width(217.dp)
                        .height(69.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Registrando..." else "REGISTRARSE",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 🔷 Overlay de carga
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.boton_principal),
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Registrando...",
                            color = colorResource(id = R.color.white),
                            fontFamily = Kavoon,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextLabel(text: String) {
    Text(
        text = text,
        color = colorResource(id = R.color.texto_principal),
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
fun InputField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Black.copy(alpha = 0.5f),
                fontFamily = Kavoon,
                fontSize = 15.sp
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
            color = Color.Black,
            fontFamily = Kavoon,
            fontSize = 16.sp
        )
    )
}

@Composable
fun RolButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF69C225)
        else colorResource(id = R.color.boton_principal).copy(alpha = 0.7f),
        label = "Animación de color"
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(220.dp)
            .height(55.dp)
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.texto_principal),
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = BungeeInline,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
