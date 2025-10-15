package com.example.inventariadosapp.ui.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.BungeeInline
import com.example.inventariadosapp.ui.theme.Kavoon



@Composable
fun WelcomeScreen(navController: NavController) {
    val activity = navController.context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.fondo_claro))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 🔹 Botón de cierre (X)
        IconButton(
            onClick = { activity?.finish() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close_roja),
                contentDescription = stringResource(id = R.string.close_app_description),
                modifier = Modifier.size(40.dp)
            )
        }

        // 🔹 Contenido principal centrado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔸 Título superior “INVENTARIADOS”
            Text(
                text = stringResource(id = R.string.title_app),
                fontFamily = BungeeInline,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = colorResource(id = R.color.texto_principal),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔸 Imagen central
            Image(
                painter = painterResource(id = R.drawable.logo_inventariados),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔸 Texto “¡BIENVENIDO!”
            Text(
                text = stringResource(id = R.string.welcome_t),
                fontFamily = Kavoon,
                fontSize = 28.sp,
                color = colorResource(id = R.color.texto_principal),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Botón “INICIAR SESIÓN”
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.boton_principal)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(id = R.string.login_button),
                    fontFamily = Kavoon,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botón “CREAR CUENTA”
            Button(
                onClick = { navController.navigate("registro") },
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.boton_principal)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(id = R.string.register_b),
                    fontFamily = Kavoon,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}
