package com.example.inventariadosapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventariadosAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "bienvenida") {
        composable("bienvenida") { PantallaBienvenida(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegisterScreen(navController) }
        composable("reset") { ResetPasswordScreen(navController) }
    }
}

@Composable
fun PantallaBienvenida(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fondo_claro))
                .padding(padding)
        ) {
            // Botón "X" (Cerrar aplicación)
            IconButton(
                onClick = { activity?.finish() },
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 11.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_close_roja),
                    contentDescription = "Cerrar aplicación"
                )
            }

            // Contenido principal centrado
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 143.dp) // posición del título según Figma
            ) {
                // Nombre de la app
                Text(
                    text = "INVENTARIADOS",
                    fontSize = 35.sp,
                    color = colorResource(id = R.color.texto_principal),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fondo circular del logo
                Box(
                    modifier = Modifier
                        .size(172.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF949EDE)), // color del círculo del Figma
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_inventariados),
                        contentDescription = "Logo Inventariados",
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Texto de bienvenida
                Text(
                    text = "¡BIENVENIDO!",
                    fontSize = 50.sp,
                    color = colorResource(id = R.color.texto_principal),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Botón Iniciar sesión
                Button(
                    onClick = { navController.navigate("login") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF949EDE)
                    ),
                    modifier = Modifier
                        .width(217.dp)
                        .height(69.dp)
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Crear cuenta
                Button(
                    onClick = { navController.navigate("registro") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF949EDE)
                    ),
                    modifier = Modifier
                        .width(217.dp)
                        .height(69.dp)
                ) {
                    Text(
                        text = "CREAR CUENTA",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}



