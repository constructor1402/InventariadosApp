package com.example.inventariadosapp.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InicioAdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _equiposDisponibles = MutableStateFlow(0)
    val equiposDisponibles: StateFlow<Int> = _equiposDisponibles

    private val _equiposEnUso = MutableStateFlow(0)
    val equiposEnUso: StateFlow<Int> = _equiposEnUso

    private val _usuariosCreados = MutableStateFlow(0)
    val usuariosCreados: StateFlow<Int> = _usuariosCreados

    init {
        cargarDatosDashboard()
    }

    fun cargarDatosDashboard() {

     
        db.collection("equipos").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Error escuchando equipos", error)
                return@addSnapshotListener
            }

            snapshot?.let { data ->
                val disponibles = data.documents.count { d ->
                    d.getString("estado")?.lowercase()?.trim() == "disponible"
                }
                val enUso = data.documents.count { d ->
                    d.getString("estado")?.lowercase()?.trim() == "en uso"
                }

                _equiposDisponibles.value = disponibles
                _equiposEnUso.value = enUso

                Log.d("Firestore", "Equipos disponibles: $disponibles, en uso: $enUso")
            }
        }


        db.collection("usuarios").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Error escuchando usuarios", error)
                return@addSnapshotListener
            }

            snapshot?.let { data ->
                val totalUsuarios = data.size()
                _usuariosCreados.value = totalUsuarios

                Log.d("Firestore", "Usuarios creados: $totalUsuarios")
            }
        }
    }
}
