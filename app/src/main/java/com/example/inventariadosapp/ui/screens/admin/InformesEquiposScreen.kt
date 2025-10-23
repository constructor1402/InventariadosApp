package com.example.inventariadosapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformesEquiposScreen(
    adminNavController: NavController,
    viewModel: InformeEquiposViewModel = viewModel()
) {
    val equipos by viewModel.equipos.collectAsState()
    var codigoBusqueda by remember { mutableStateOf("") }
    var estadoSeleccionado by remember { mutableStateOf("") }

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
                        onClick = { adminNavController.navigate("informes_admin") },
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
                "Nombre o Código de equipo",
                fontFamily = Kavoon,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            TextField(
                value = codigoBusqueda,
                onValueChange = {codigoBusqueda = it},
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
            DropdownEstado(
                estadoSeleccionado = estadoSeleccionado,
                onEstadoChange = { estadoSeleccionado = it }
            )

            // Botón Buscar
            Button(
                onClick = {
                    viewModel.buscarEquipos(codigoBusqueda, estadoSeleccionado)
                    adminNavController.navigate("resultados_informe")
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


            Spacer(Modifier.height(12.dp))


            //viewModel.TablaEquiposFirebase(equipos)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownEstado(
    estadoSeleccionado: String,
    onEstadoChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var estadoSeleccionado by remember { mutableStateOf("Disponible") }
    val opciones = listOf("Asignado", "Dañado")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(12.dp)) // 🔹 borde redondeado en todo el dropdown
            .background(colorResource(id = R.color.campo_fondo))
    ) {
        TextField(
            value = estadoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Estado", fontFamily = Kavoon) },
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
            shape = RoundedCornerShape(12.dp) // 🔹 redondeo también del TextField
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp)) // 🔹 redondeo del menú desplegable
                .background(colorResource(id = R.color.campo_fondo))
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, fontFamily = Kavoon) },
                    onClick = {
                        estadoSeleccionado = opcion
                        expanded = false
                    }
                )
            }
        }
    }

}


