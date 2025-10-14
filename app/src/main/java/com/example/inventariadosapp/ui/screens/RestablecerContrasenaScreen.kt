package com.example.inventariadosapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavBackStackEntry
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RestablecerContrasenaScreen(navController: NavController, telefono: String? = null) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(padding)
        ) {
            // 🔙 Botón volver
            IconButton(
                onClick = { navController.popBackStack() },
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

            // 🔹 Contenido
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Restablecer contraseña",
                    fontFamily = Kavoon,
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.texto_principal),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    placeholder = { Text("Nueva contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    placeholder = { Text("Confirmar contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (nuevaContrasena != confirmarContrasena) {
                            Toast.makeText(context, "⚠️ Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (telefono.isNullOrBlank()) {
                            Toast.makeText(context, "⚠️ No se detectó el número de teléfono", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        db.collection("usuarios")
                            .whereEqualTo("numeroCelular", telefono)
                            .get()
                            .addOnSuccessListener { result ->
                                if (!result.isEmpty) {
                                    val docId = result.documents[0].id
                                    db.collection("usuarios").document(docId)
                                        .update("contrasena", nuevaContrasena)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            Toast.makeText(context, "✅ Contraseña actualizada", Toast.LENGTH_LONG).show()
                                            navController.navigate("login")
                                        }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "❌ Número no encontrado", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Guardando..." else "RESTABLECER",
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
