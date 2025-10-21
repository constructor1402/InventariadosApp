package com.example.inventariadosapp.ui.screens.admin.gestion.equipos

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Equipo(
    val serial: String = "",
    val referencia: String = "",
    val tipo: String = "",
    val fechaCertificacion: String = "",
    val certificadoUrl: String = ""
)

class EquiposViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // 🔹 Variables de UI
    var serial by mutableStateOf("")
    var referencia by mutableStateOf("")
    var tipo by mutableStateOf("")
    var fecha by mutableStateOf("")
    var certificadoUrl: Uri? = null

    // 🔹 Mensajes dinámicos
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    // 🧹 Limpia el mensaje después de mostrarlo
    fun limpiarMensaje() {
        _mensaje.value = ""
    }

    // 🔸 GUARDAR EQUIPO
    fun guardarEquipo() {
        if (serial.isBlank() || referencia.isBlank() || tipo.isBlank() || fecha.isBlank()) {
            _mensaje.value = "⚠️ Por favor completa todos los campos."
            return
        }

        viewModelScope.launch {
            try {
                val doc = db.collection("equipos").document(serial).get().await()
                if (doc.exists()) {
                    _mensaje.value = "⚠️ Ya existe un equipo con este serial."
                    return@launch
                }

                val equipo = hashMapOf(
                    "serial" to serial,
                    "referencia" to referencia,
                    "tipo" to tipo,
                    "fechaCertificacion" to fecha,
                    "certificadoUrl" to certificadoUrl?.toString().orEmpty()
                )

                db.collection("equipos").document(serial).set(equipo).await()
                _mensaje.value = "✅ Equipo guardado correctamente."
                limpiarCampos()

            } catch (e: Exception) {
                _mensaje.value = "❌ Error al guardar: ${e.message}"
            }
        }
    }

    // 🔸 BUSCAR EQUIPO
    fun buscarEquipo() {
        if (serial.isBlank()) {
            _mensaje.value = "⚠️ Ingresa un serial para buscar."
            return
        }

        viewModelScope.launch {
            try {
                val doc = db.collection("equipos").document(serial).get().await()
                if (doc.exists()) {
                    referencia = doc.getString("referencia") ?: ""
                    tipo = doc.getString("tipo") ?: ""
                    fecha = doc.getString("fechaCertificacion") ?: ""
                    _mensaje.value = "✅ Equipo encontrado."
                } else {
                    _mensaje.value = "⚠️ No se encontró el equipo."
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al buscar: ${e.message}"
            }
        }
    }

    // 🔸 ELIMINAR EQUIPO
    fun eliminarEquipo() {
        if (serial.isBlank()) {
            _mensaje.value = "⚠️ Ingresa el serial del equipo a eliminar."
            return
        }

        viewModelScope.launch {
            try {
                val doc = db.collection("equipos").document(serial).get().await()
                if (!doc.exists()) {
                    _mensaje.value = "⚠️ No se encontró el equipo para eliminar."
                    return@launch
                }

                db.collection("equipos").document(serial).delete().await()
                _mensaje.value = "🗑️ Equipo eliminado correctamente."
                limpiarCampos()
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al eliminar: ${e.message}"
            }
        }
    }

    // 🔸 SUBIR CERTIFICADO (solo selecciona archivo localmente)
    fun subirCertificado(uri: Uri) {
        certificadoUrl = uri
        _mensaje.value = "📄 Archivo seleccionado: ${uri.lastPathSegment}"
    }

    // 🔹 Limpia todos los campos
    private fun limpiarCampos() {
        serial = ""
        referencia = ""
        tipo = ""
        fecha = ""
        certificadoUrl = null
    }
}
