package com.example.inventariadosapp.screens.admin.gestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

data class Obra(
    val idObra: String = "",
    val nombreObra: String = "",
    val ubicacion: String = "",
    val clienteNombre: String = ""
)

class ObraViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Campos de formulario
    var idObra by mutableStateOf("")
        private set
    var nombreObra by mutableStateOf("")
        private set
    var ubicacion by mutableStateOf("")
        private set
    var clienteNombre by mutableStateOf("")
        private set

    // Validaciones y mensajes
    var nombreObraError by mutableStateOf(false)
    var ubicacionError by mutableStateOf(false)
    var mensajeStatus by mutableStateOf<String?>(null)

    // --- Actualizar campos ---
    fun updateNombreObra(value: String) {
        nombreObra = value.trim()
        nombreObraError = false
    }

    fun updateUbicacion(value: String) {
        ubicacion = value.trim()
        ubicacionError = false
    }

    fun updateClienteNombre(value: String) {
        clienteNombre = value.trim()
    }

    // --- Guardar con validaciones y control de duplicados ---
    fun guardarObra() {
        // Validaciones
        if (nombreObra.isBlank()) {
            nombreObraError = true
            mensajeStatus = "⚠️ El nombre de la obra es obligatorio."
            return
        }
        if (ubicacion.isBlank()) {
            ubicacionError = true
            mensajeStatus = "⚠️ La ubicación es obligatoria."
            return
        }

        // Verificar duplicado antes de guardar
        db.collection("obras")
            .whereEqualTo("nombreObra", nombreObra)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    mensajeStatus = "⚠️ Ya existe una obra con este nombre."
                } else {
                    // Si no existe, crear una nueva
                    val idGenerado = UUID.randomUUID().toString()
                    val obra = hashMapOf(
                        "idObra" to idGenerado,
                        "nombreObra" to nombreObra,
                        "ubicacion" to ubicacion,
                        "clienteNombre" to clienteNombre
                    )

                    db.collection("obras")
                        .document(idGenerado)
                        .set(obra)
                        .addOnSuccessListener {
                            mensajeStatus = "✅ Obra registrada correctamente."
                            limpiarCampos()
                        }
                        .addOnFailureListener {
                            mensajeStatus = "❌ Error al registrar la obra."
                        }
                }
            }
            .addOnFailureListener {
                mensajeStatus = "❌ Error al verificar duplicados."
            }
    }

    // --- Buscar por nombre ---
    fun buscarObra(nombre: String) {
        if (nombre.isBlank()) {
            mensajeStatus = "⚠️ Ingresa un nombre de obra para buscar."
            return
        }

        db.collection("obras")
            .whereEqualTo("nombreObra", nombre)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val doc = result.documents.first()
                    idObra = doc.getString("idObra") ?: ""
                    nombreObra = doc.getString("nombreObra") ?: ""
                    ubicacion = doc.getString("ubicacion") ?: ""
                    clienteNombre = doc.getString("clienteNombre") ?: ""
                    mensajeStatus = "✅ Obra encontrada."
                } else {
                    mensajeStatus = "❌ No se encontró ninguna obra con ese nombre."
                }
            }
            .addOnFailureListener {
                mensajeStatus = "❌ Error al buscar la obra."
            }
    }

    // --- Eliminar obra ---
    fun eliminarObra() {
        if (idObra.isBlank()) {
            mensajeStatus = "⚠️ Primero busca la obra para poder eliminarla."
            return
        }

        db.collection("obras")
            .document(idObra)
            .delete()
            .addOnSuccessListener {
                limpiarCampos()
                mensajeStatus = "🗑️ Obra eliminada correctamente."
            }
            .addOnFailureListener {
                mensajeStatus = "❌ Error al eliminar la obra."
            }
    }

    // --- Limpiar formulario ---
    fun limpiarCampos() {
        idObra = ""
        nombreObra = ""
        ubicacion = ""
        clienteNombre = ""
        mensajeStatus = null
        nombreObraError = false
        ubicacionError = false
    }
}
