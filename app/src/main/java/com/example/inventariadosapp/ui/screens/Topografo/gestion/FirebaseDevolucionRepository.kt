package com.example.inventariadosapp.ui.screens.Topografo.gestion

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class EquipoModel(
    val id: String = "",
    val serial: String? = null,
    val referencia: String? = null,
    val tipo: String? = null,
    val other: Map<String, Any>? = null
)

class FirebaseDevolucionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val collectionName = "equipos"

    // Buscar equipo por serial
    suspend fun buscarPorSerial(serial: String): EquipoModel? {
        val querySnapshot = firestore.collection(collectionName)
            .whereEqualTo("serial", serial)
            .get()
            .await()

        val doc = querySnapshot.documents.firstOrNull() ?: return null
        val map = doc.data ?: emptyMap()
        return EquipoModel(
            id = doc.id,
            serial = map["serial"] as? String,
            referencia = map["referencia"] as? String,
            tipo = map["tipo"] as? String,
            other = map
        )
    }

    // Actualizar estado a "disponible"
    suspend fun marcarComoDisponible(id: String): Result<Unit> {
        return try {
            val updates = mapOf<String, Any>(
                "estado" to "disponible",

            )
            firestore.collection(collectionName)
                .document(id)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}