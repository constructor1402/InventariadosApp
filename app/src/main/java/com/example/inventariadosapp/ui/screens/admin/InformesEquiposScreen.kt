package com.example.inventariadosapp.ui.screens.admin

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun InformesEquiposScreen(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel = viewModel()
) {
    val equipos by viewModel.equipos.collectAsState()



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.fondo_claro)),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.fondo_claro)),
                title = { Text("Informe de equipos", fontFamily = Kavoon, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { adminNavController.navigate("informes_admin") },
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // Tabla con los datos de Firestore
            viewModel.TablaEquiposFirebase(equipos)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { /* generar informe */ },
                colors = ButtonDefaults.buttonColors(Color(0xFF6686E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painterResource(id = R.drawable.ic_informes), contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Generar informe", color = Color.White, fontFamily = Kavoon)
            }
        }
    }


}