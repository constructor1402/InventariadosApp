package com.example.inventariadosapp.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class InformeEquiposViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()

    init {
        cargarEquipos()
    }

    fun cargarEquipos() {
        db.collection("equipos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    doc.toObject(Equipo::class.java)
                }
                _equipos.value = lista
            }
    }
    @Composable
    fun TablaEquiposFirebase(equipos: List<Equipo>) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Código", "Nombre", "Ubicación", "Estado").forEach {
                    Text(it, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
            }
            equipos.forEach { eq ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(eq.serial, modifier = Modifier.weight(1f))
                    Text(eq.referencia, modifier = Modifier.weight(1f))
                    Text(eq.tipo, modifier = Modifier.weight(1f))
                    Text(eq.fechaCertificacion, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


