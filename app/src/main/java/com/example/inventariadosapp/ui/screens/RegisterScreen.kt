package com.example.inventariadosapp

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.theme.BungeeInline
import com.example.inventariadosapp.ui.theme.Kavoon
import androidx.compose.animation.animateColorAsState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import android.widget.Toast


@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val db = FirebaseFirestore.getInstance()


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
            // Botón para volver
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

            // Contenido principal con scroll
            val scrollState = rememberScrollState()

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

                // Nombre completo
                TextLabel("Nombre completo")
                InputField(nombre, "Nombre completo") { nombre = it }

                // Número de celular
                TextLabel("Número de celular")
                InputField(celular, "Número de celular") { celular = it }

                // Correo electrónico
                TextLabel("Correo electrónico")
                InputField(correo, "Correo electrónico") { correo = it }

                // Contraseña
                TextLabel("Contraseña")
                InputField(contrasena, "Contraseña") { contrasena = it }

                // Confirmar contraseña
                TextLabel("Confirmar contraseña")
                InputField(confirmar, "Confirmar contraseña") { confirmar = it }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de rol
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


                // Botón registrarse
                Button(
                    onClick = {
                        if (nombre.isNotBlank() && celular.isNotBlank() && correo.isNotBlank() &&
                            contrasena.isNotBlank() && confirmar.isNotBlank() && rolSeleccionado.isNotBlank()
                        ) {
                            if (contrasena == confirmar) {
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
                                        Toast.makeText(
                                            context,
                                            "✅ Usuario registrado correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // Redirige al login solo si el NavController está activo
                                        try {
                                            navController.navigate("login")
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Error al navegar: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            context,
                                            "❌ Error al registrar: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
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
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "REGISTRARSE",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}

@Composable
fun TextLabel(text: String) {
    Text(
        text = text,
        color = Color.Black,
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
fun InputField(value: String, placeholder: String, onValueChange: (String) -> Unit) {
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
    // Animación de color suave
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
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = BungeeInline,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}
