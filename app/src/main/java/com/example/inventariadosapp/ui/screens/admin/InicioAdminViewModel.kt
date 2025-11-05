package com.example.inventariadosapp.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InicioAdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _equiposDisponibles = MutableStateFlow(0)
    val equiposDisponibles: StateFlow<Int> = _equiposDisponibles

    private val _equiposAsignados = MutableStateFlow(0)
    val equiposAsignados: StateFlow<Int> = _equiposAsignados

    private val _usuariosCreados = MutableStateFlow(0)
    val usuariosCreados: StateFlow<Int> = _usuariosCreados

    init {
        cargarDatosDashboard()
    }

    private fun cargarDatosDashboard() {
        viewModelScope.launch {
            // Escucha en tiempo real los cambios de la colección de equipos
            db.collection("equipos").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error al escuchar equipos", error)
                    return@addSnapshotListener
                }

                snapshot?.let { data ->
                    val disponibles = data.documents.count { it.getString("estado")?.lowercase() == "disponible" }
                    val asignados = data.documents.count { it.getString("estado")?.lowercase() == "asignado" }

                    _equiposDisponibles.value = disponibles
                    _equiposAsignados.value = asignados
                }
            }

            // Escucha en tiempo real los cambios de la colección de usuarios
            db.collection("usuarios").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error al escuchar usuarios", error)
                    return@addSnapshotListener
                }

                _usuariosCreados.value = snapshot?.size() ?: 0
            }
        }
    }
}
