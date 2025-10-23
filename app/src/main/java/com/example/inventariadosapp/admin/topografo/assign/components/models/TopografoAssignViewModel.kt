package com.example.inventariadosapp.admin.topografo.assign.components.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopografoAssignViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _serial = MutableStateFlow("")
    val serial: StateFlow<String> = _serial

    private val _referencia = MutableStateFlow("")
    val referencia: StateFlow<String> = _referencia

    private val _tipo = MutableStateFlow("")
    val tipo: StateFlow<String> = _tipo

    private val _obra = MutableStateFlow("")
    val obra: StateFlow<String> = _obra

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error


    fun updateSerial(value: String) {
        _serial.value = value
    }

    fun updateReferencia(value: String) {
        _referencia.value = value
    }

    fun updateTipo(value: String) {
        _tipo.value = value
    }

    fun updateObra(value: String) {
        _obra.value = value
    }


    // 🔎 Buscar equipo por campo 'serial' en Firestore
    fun buscarEquipo(serial: String) {
        _mensaje.value = ""
        _error.value = ""

        viewModelScope.launch {
            db.collection("equipos")
                .whereEqualTo("serial", serial) // 👈 Cambio clave
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val doc = result.documents.first()
                        _referencia.value = doc.getString("referencia") ?: ""
                        _tipo.value = doc.getString("tipo") ?: ""
                        _mensaje.value = "✅ Equipo encontrado"
                    } else {
                        _referencia.value = ""
                        _tipo.value = ""
                        _error.value = "⚠️ No se encontró el equipo con serial: $serial"
                    }
                }
                .addOnFailureListener {
                    _error.value = "❌ Error al buscar equipo"
                }
        }
    }


    fun guardarAsignacion() {
        val serialVal = _serial.value.trim()
        val obraVal = _obra.value.trim()

        if (serialVal.isEmpty() || obraVal.isEmpty()) {
            _error.value = "⚠️ Debes llenar todos los campos"
            return
        }

        if (_referencia.value.isEmpty() || _tipo.value.isEmpty()) {
            _error.value = "⚠️ Debes escanear un equipo válido antes de asignar."
            return
        }

        viewModelScope.launch {
            val data = mapOf(
                "serial" to serialVal,
                "referencia" to _referencia.value,
                "tipo" to _tipo.value,
                "obra" to obraVal,
                "estado" to "Asignado",
                "asignadoPor" to "Usuario no identificado",
                "fechaAsignacion" to com.google.firebase.Timestamp.now()
            )

            db.collection("asignaciones").add(data)
                .addOnSuccessListener {
                    _mensaje.value = "✅ El equipo ha sido asignado correctamente"
                    _serial.value = ""
                    _referencia.value = ""
                    _tipo.value = ""
                    _obra.value = ""
                }
                .addOnFailureListener {
                    _error.value = "❌ Error al asignar el equipo"
                }
        }
    }
}
