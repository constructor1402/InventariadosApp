package com.example.inventariadosapp.ui.screens.Topografo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InicioTopografoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _equiposDisponibles = MutableStateFlow(0)
    val equiposDisponibles: StateFlow<Int> = _equiposDisponibles

    fun cargarEquiposDisponibles() {
        viewModelScope.launch {
            try {
                val query = db.collection("equipos")
                    .whereEqualTo("estado", "Disponible")
                    .get()
                    .await()
                _equiposDisponibles.value = query.size()
            } catch (e: Exception) {
                _equiposDisponibles.value = 0
            }
        }
    }
}
