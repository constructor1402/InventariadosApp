package com.example.inventariadosapp.admin.topografo.assign.components.models // <-- PACKAGE CORREGIDO
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

class TopografoAssignViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Campos observables
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

    // Llama a esta función después de escanear el serial
    fun buscarEquipo(serial: String) {
        // Limpiar mensajes anteriores
        _mensaje.value = ""
        _error.value = ""

        viewModelScope.launch {
            db.collection("equipos").document(serial).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        _referencia.value = doc.getString("referencia") ?: ""
                        _tipo.value = doc.getString("tipo") ?: ""
                        _mensaje.value = "✅ Equipo encontrado"
                    } else {
                        // Limpiar campos si no se encuentra
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

        // Asumiendo que solo se guarda si el equipo ha sido encontrado previamente
        if (_referencia.value.isEmpty() || _tipo.value.isEmpty()) {
            _error.value = "⚠️ Debes escanear un equipo válido antes de asignar."
            return
        }

        viewModelScope.launch {
            val data = mapOf(
                "serial" to serialVal,
                "referencia" to _referencia.value,
                "tipo" to _tipo.value,
                "obra" to obraVal
            )

            db.collection("asignaciones").add(data)
                .addOnSuccessListener {
                    _mensaje.value = "✅ El equipo ha sido asignado correctamente"
                    // Limpiar campos después de guardar si es necesario
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