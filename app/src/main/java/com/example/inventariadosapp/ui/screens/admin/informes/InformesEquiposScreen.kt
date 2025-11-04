package com.example.inventariadosapp.ui.screens.admin.informes

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventariadosapp.R
import com.example.inventariadosapp.ui.theme.Kavoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformesEquiposScreen(
    adminNavController: NavController,
    userCorreo: String,
    viewModel: InformeEquiposViewModel = viewModel(LocalContext.current as ComponentActivity)

) {
    var serial by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.fondo_claro)),
                title = {
                    Text(
                        "Informe de equipos",
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { adminNavController.navigate("informes_admin/$userCorreo") },
                        modifier = Modifier.padding(start = 8.dp)
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
        },
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.fondo_claro))
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)

        ) {
            // Ícono superior
            Icon(
                painter = painterResource(id = R.drawable.icon_device),
                contentDescription = "Equipos",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(90.dp)
                    .padding(top = 8.dp)
            )

            // Campo de búsqueda (opcional)
            Text(
                "Serial de equipo",
                fontFamily = Kavoon,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            TextField(
                value = serial,
                onValueChange = {serial = it},
                placeholder = {
                    Text(
                        "Opcional",
                        fontFamily = Kavoon,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF8D8EB5),
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.campo_fondo),
                    unfocusedContainerColor = colorResource(id = R.color.campo_fondo),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_filter),
                        contentDescription = "Filtrar",
                        tint = Color.Unspecified
                    )
                }
            )

            // Dropdowns: Estado y Categoría
            DropdownTipo(
                tipoSeleccionado = tipo,
                onTypeChange = { tipo = it }
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.buscarEquipos(serial, tipo)
                        adminNavController.navigate("resultados_informe/$userCorreo")
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.azul_admin)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(55.dp)
                    .padding(top = 12.dp)
            ) {
                Text(
                    "Buscar",
                    fontFamily = Kavoon,
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTipo(
    tipoSeleccionado: String,
    onTypeChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Herramienta", "Maquinaria", "Vehículo")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.campo_fondo))
    ) {
        TextField(
            value = tipoSeleccionado.ifBlank { "Tipo..." },
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo", fontFamily = Kavoon) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.campo_fondo),
                unfocusedContainerColor = colorResource(id = R.color.campo_fondo),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(id = R.color.campo_fondo))
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, fontFamily = Kavoon) },
                    onClick = {
                        onTypeChange(opcion) // ✅ se comunica hacia la pantalla padre
                        expanded = false
                    }
                )
            }
        }
    }
}
