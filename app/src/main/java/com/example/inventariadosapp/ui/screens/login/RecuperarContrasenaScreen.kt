package com.example.inventariadosapp.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun RecuperarContrasenaScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var telefono by remember { mutableStateOf("") }
    var codigoGenerado by remember { mutableStateOf("") }
    var codigoIngresado by remember { mutableStateOf("") }
    var isCodigoMostrado by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 70.dp)
            ) {
                Text(
                    text = "Recuperar contraseña",
                    fontFamily = Kavoon,
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.texto_principal),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 📱 Campo teléfono
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    placeholder = { Text("Ejemplo: +573121110000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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

                Spacer(modifier = Modifier.height(16.dp))

                // 🔘 Botón generar código
                Button(
                    onClick = {
                        if (telefono.isBlank()) {
                            Toast.makeText(context, "⚠️ Ingresa un número válido", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        val codigo = (100000..999999).random().toString()

                        // Guardar código en Firestore
                        val data = hashMapOf(
                            "telefono" to telefono,
                            "codigo" to codigo,
                            "timestamp" to Date()
                        )

                        db.collection("codigos_recuperacion")
                            .document(telefono)
                            .set(data)
                            .addOnSuccessListener {
                                codigoGenerado = codigo
                                isCodigoMostrado = true
                                isLoading = false
                                Toast.makeText(context, "📩 Código generado correctamente", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "❌ Error al generar código", Toast.LENGTH_SHORT).show()
                            }
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Generando..." else "GENERAR CÓDIGO",
                        fontFamily = Kavoon,
                        color = colorResource(id = R.color.texto_principal),
                        fontSize = 16.sp
                    )
                }

                // 🔢 Mostrar código en modo prueba
                if (isCodigoMostrado) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Tu código de verificación es:",
                        color = colorResource(id = R.color.texto_principal),
                        fontFamily = Kavoon,
                        fontSize = 16.sp
                    )
                    Text(
                        text = codigoGenerado,
                        color = Color.Red,
                        fontFamily = Kavoon,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 🧩 Campo para ingresar el código
                OutlinedTextField(
                    value = codigoIngresado,
                    onValueChange = { codigoIngresado = it },
                    placeholder = { Text("Código de verificación") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                Spacer(modifier = Modifier.height(25.dp))

                // ⏭️ Botón Siguiente
                Button(
                    onClick = {
                        if (codigoIngresado == codigoGenerado && codigoIngresado.isNotBlank()) {
                            Toast.makeText(context, "✅ Código verificado", Toast.LENGTH_SHORT).show()
                            navController.navigate("restablecer_contrasena/$telefono")
                        } else {
                            Toast.makeText(context, "❌ Código incorrecto", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton_principal)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "SIGUIENTE",
                        fontFamily = Kavoon,
                        color = colorResource(id = R.color.texto_principal),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
