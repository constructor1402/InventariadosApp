package com.example.inventariadosapp.screens.admin.gestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Obra(
    val idObra: String = "",
    val nombreObra: String = "",
    val ubicacion: String = "",
    val clienteNombre: String = ""
)

class ObraViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Campos observables para la UI
    var nombreObra by mutableStateOf("")
        private set
    var ubicacion by mutableStateOf("")
        private set
    var clienteNombre by mutableStateOf("")
        private set

    // Mensaje dinámico
    var mensaje by mutableStateOf("")
        private set

    // Validaciones visuales
    var nombreObraError by mutableStateOf(false)
        private set
    var ubicacionError by mutableStateOf(false)
        private set

    // 🔹 Actualizadores
    fun updateNombreObra(valor: String) {
        nombreObra = valor
        nombreObraError = false
    }

    fun updateUbicacion(valor: String) {
        ubicacion = valor
        ubicacionError = false
    }

    fun updateCliente(valor: String) {
        clienteNombre = valor
    }

    // 🔹 GUARDAR (con validación de duplicado y normalización)
    fun guardarObra() {
        if (nombreObra.isBlank()) {
            nombreObraError = true
            mensaje = "⚠️ El nombre de la obra es obligatorio."
            return
        }
        if (ubicacion.isBlank()) {
            ubicacionError = true
            mensaje = "⚠️ La ubicación es obligatoria."
            return
        }

        val obrasRef = db.collection("obras")
        val nombreNormalizado = nombreObra.trim().lowercase()

        // Verificar duplicados ignorando mayúsculas/minúsculas
        obrasRef.whereEqualTo("nombreObraLower", nombreNormalizado)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    mensaje = "⚠️ Ya existe una obra con ese nombre."
                } else {
                    val nuevaObra = hashMapOf(
                        "idObra" to db.collection("obras").document().id,
                        "nombreObra" to nombreObra.trim(),
                        "nombreObraLower" to nombreNormalizado,
                        "ubicacion" to ubicacion.trim(),
                        "clienteNombre" to clienteNombre.trim()
                    )

                    obrasRef.add(nuevaObra)
                        .addOnSuccessListener {
                            mensaje = "✅ Obra guardada correctamente."
                            limpiarCampos()
                        }
                        .addOnFailureListener {
                            mensaje = "❌ Error al guardar la obra: ${it.message}"
                        }
                }
            }
            .addOnFailureListener {
                mensaje = "❌ Error al verificar duplicados: ${it.message}"
            }
    }

    // 🔹 BUSCAR (sin importar mayúsculas/minúsculas)
    fun buscarObra() {
        if (nombreObra.isBlank()) {
            mensaje = "⚠️ Escribe el nombre de la obra a buscar."
            return
        }

        val nombreNormalizado = nombreObra.trim().lowercase()

        db.collection("obras")
            .whereEqualTo("nombreObraLower", nombreNormalizado)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    mensaje = "❌ No se encontró la obra."
                } else {
                    val doc = snapshot.documents.first()
                    nombreObra = doc.getString("nombreObra") ?: ""
                    ubicacion = doc.getString("ubicacion") ?: ""
                    clienteNombre = doc.getString("clienteNombre") ?: ""
                    mensaje = "✅ Obra encontrada y cargada."
                }
            }
            .addOnFailureListener {
                mensaje = "❌ Error al buscar: ${it.message}"
            }
    }

    // 🔹 ELIMINAR (por nombre, sin importar mayúsculas)
    fun eliminarObra() {
        if (nombreObra.isBlank()) {
            mensaje = "⚠️ Escribe el nombre de la obra a eliminar."
            return
        }

        val nombreNormalizado = nombreObra.trim().lowercase()
        val obrasRef = db.collection("obras")

        obrasRef.whereEqualTo("nombreObraLower", nombreNormalizado)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    mensaje = "❌ No se encontró la obra para eliminar."
                } else {
                    val id = snapshot.documents.first().id
                    obrasRef.document(id).delete()
                        .addOnSuccessListener {
                            mensaje = "🗑️ Obra eliminada correctamente."
                            limpiarCampos()
                        }
                        .addOnFailureListener {
                            mensaje = "❌ Error al eliminar la obra: ${it.message}"
                        }
                }
            }
            .addOnFailureListener {
                mensaje = "❌ Error al buscar la obra: ${it.message}"
            }
    }

    // 🔹 Limpiar campos después de guardar/eliminar
    private fun limpiarCampos() {
        nombreObra = ""
        ubicacion = ""
        clienteNombre = ""
        nombreObraError = false
        ubicacionError = false
    }
}
