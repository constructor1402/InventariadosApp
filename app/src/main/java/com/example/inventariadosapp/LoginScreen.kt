package com.example.inventariadosapp

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.inventariadosapp.ui.theme.Kavoon

@Composable
fun LoginScreen(navController: NavController) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val db = FirebaseFirestore.getInstance()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // 🔙 Flecha volver
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🟣 Título
                Text(
                    text = "Inicio de Sesión",
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.texto_principal),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🟣 Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_inventariados),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo correo
                Text(
                    text = "Correo electrónico",
                    fontFamily = Kavoon,
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("Ingresa tu correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.boton_principal),
                        unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo contraseña
                Text(
                    text = "Contraseña",
                    fontFamily = Kavoon,
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    placeholder = { Text("Ingresa tu contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible)
                            painterResource(id = R.drawable.ic_visibility_off)
                        else
                            painterResource(id = R.drawable.ic_visibility)

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = icon, contentDescription = "Mostrar u ocultar contraseña")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.boton_principal),
                        unfocusedContainerColor = colorResource(id = R.color.boton_principal),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 🟠 Botón iniciar sesión
                Button(
                    onClick = {
                        if (correo.isNotBlank() && contrasena.isNotBlank()) {
                            db.collection("usuarios")
                                .whereEqualTo("correoElectronico", correo)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (documents.isEmpty) {
                                        Toast.makeText(
                                            navController.context,
                                            "El correo no está registrado.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val userDoc = documents.first()
                                        val storedPassword = userDoc.getString("contrasena")

                                        if (storedPassword == contrasena) {
                                            Toast.makeText(
                                                navController.context,
                                                "Inicio de sesión exitoso",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Aquí definimos a qué pantalla ir según el rol
                                            val rol = userDoc.getString("rolSeleccionado")
                                            when (rol) {
                                                "Admin" -> navController.navigate("panel_admin")
                                                "Topógrafo" -> navController.navigate("panel_topografo")
                                                "Consulta" -> navController.navigate("panel_consulta")
                                                else -> navController.navigate("bienvenida")
                                            }
                                        } else {
                                            Toast.makeText(
                                                navController.context,
                                                "Contraseña incorrecta.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        navController.context,
                                        "Error al verificar usuario: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        fontFamily = Kavoon,
                        color = colorResource(id = R.color.texto_principal),
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Enlace de registro
                TextButton(onClick = { navController.navigate("registro") }) {
                    Text(
                        text = "¿No tienes cuenta? Regístrate",
                        color = Color.Black,
                        fontFamily = Kavoon,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
