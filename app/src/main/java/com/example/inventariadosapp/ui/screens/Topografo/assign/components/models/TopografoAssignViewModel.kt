package com.example.inventariadosapp.ui.screens.Topografo.assign.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

// 🧩 Modelo de datos del equipo
data class EquipoAsignado(
    val serial: String = "",
    val estado: String = "",
    val obra: String = "",
    val tipo: String = "",
    val referencia: String = ""
)

// 🧩 Modelo de datos del historial de asignación
data class AsignacionLog(
    val serial: String,
    val tipo: String,
    val referencia: String,
    val obraAsignada: String,
    val fechaAsignacion: Timestamp,
    val estadoPrevio: String,
    val usuarioUid: String,
    val usuarioEmail: String,
    val usuarioNombre: String
)

class TopografoAssignViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var selectedEquipo by mutableStateOf(EquipoAsignado())
        private set
    var isLoading by mutableStateOf(false)
        private set
    val listaDeObras = mutableStateListOf<String>()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        cargarObras()
    }

    // 🔹 Limpia la selección actual
    fun clearSelectedEquipo() {
        selectedEquipo = EquipoAsignado()
    }

    // 🔹 Carga todas las obras disponibles
    private fun cargarObras() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("obras").get().await()
                val obras = snapshot.documents.mapNotNull { it.getString("nombreObra") }
                listaDeObras.clear()
                listaDeObras.addAll(obras)
            } catch (e: Exception) {
                println("Error al cargar obras: ${e.message}")
            }
        }
    }

    // 🔹 Obtiene los datos del equipo según el serial escaneado
    fun fetchEquipoData(serial: String, onComplete: (Boolean) -> Unit = {}) {
        if (serial.isBlank()) {
            onComplete(false)
            return
        }

        isLoading = true
        val upperCaseSerial = serial.uppercase()

        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("equipos")
                    .whereEqualTo("serial", upperCaseSerial)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val doc = querySnapshot.documents[0]
                    selectedEquipo = EquipoAsignado(
                        serial = doc.getString("serial") ?: upperCaseSerial,
                        estado = doc.getString("estado") ?: "Desconocido",
                        obra = doc.getString("obra") ?: "Sin asignar",
                        tipo = doc.getString("tipo") ?: "Sin tipo",
                        referencia = doc.getString("referencia") ?: "Sin referencia"
                    )
                    _uiEvent.emit("Equipo encontrado ✅")
                    onComplete(true)
                } else {
                    selectedEquipo = EquipoAsignado(serial = upperCaseSerial)
                    _uiEvent.emit("Serial no encontrado en la base de datos ❌")
                    onComplete(false)
                }
            } catch (e: Exception) {
                selectedEquipo = EquipoAsignado(serial = upperCaseSerial)
                _uiEvent.emit("Error al obtener datos: ${e.message}")
                onComplete(false)
            } finally {
                isLoading = false
            }
        }
    }

    // 🔹 Cambia la obra seleccionada
    fun onObraChange(value: String) {
        selectedEquipo = selectedEquipo.copy(obra = value)
    }

    // 🔹 Guarda la asignación del equipo y registra el historial
    fun guardarAsignacion(navController: NavHostController) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            viewModelScope.launch { _uiEvent.emit("Error: No se pudo identificar al usuario.") }
            return
        }

        if (selectedEquipo.serial.isEmpty() || selectedEquipo.obra.isEmpty() || selectedEquipo.obra == "Seleccione La Obra") {
            viewModelScope.launch { _uiEvent.emit("Error: Faltan datos del equipo u obra.") }
            return
        }

        if (selectedEquipo.estado.equals("Asignado", ignoreCase = true)) {
            viewModelScope.launch { _uiEvent.emit("⚠️ Este equipo ya está asignado.") }
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                // 🔹 Buscar usuario por UID o correo electrónico
                val userQuery = db.collection("usuarios")
                    .whereEqualTo("correoElectronico", currentUser.email)
                    .get()
                    .await()

                val userDoc = if (!userQuery.isEmpty) userQuery.documents.first() else
                    db.collection("usuarios").document(currentUser.uid).get().await()

                val userName = userDoc?.getString("nombreCompleto") ?: currentUser.email ?: "Usuario desconocido"
                val userEmail = userDoc?.getString("correoElectronico") ?: currentUser.email ?: "Sin email"

                // 🔹 Crear registro de historial
                val log = AsignacionLog(
                    serial = selectedEquipo.serial,
                    tipo = selectedEquipo.tipo,
                    referencia = selectedEquipo.referencia,
                    obraAsignada = selectedEquipo.obra,
                    fechaAsignacion = Timestamp(Date()),
                    estadoPrevio = selectedEquipo.estado,
                    usuarioUid = currentUser.uid,
                    usuarioEmail = userEmail,
                    usuarioNombre = userName
                )

                // 🔹 Guardar historial
                db.collection("asignaciones")
                    .add(log)
                    .await()

                // 🔹 Actualizar estado del equipo
                db.collection("equipos")
                    .document(selectedEquipo.serial)
                    .update(
                        mapOf(
                            "obra" to selectedEquipo.obra,
                            "estado" to "Asignado"
                        )
                    )
                    .await()

                _uiEvent.emit("✅ Equipo asignado correctamente")
                clearSelectedEquipo()
                navController.popBackStack()

            } catch (e: Exception) {
                _uiEvent.emit("Error al guardar la asignación: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
