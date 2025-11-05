package com.example.inventariadosapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.inventariadosapp.domain.model.Obra



class ObraRepository {

    private val db = FirebaseFirestore.getInstance()
    private val obrasCollection = db.collection("obras")

    // 🔹 Guardar o actualizar obra
    suspend fun guardarObra(obra: Obra, esActualizacion: Boolean = false) {
        val nombreNormalizado = obra.nombreObra.trim().lowercase()

        // 🔹 Buscar si ya existe una obra con el mismo nombre
        val query = obrasCollection
            .whereEqualTo("nombreObraLower", nombreNormalizado)
            .get()
            .await()

        if (query.isEmpty) {
            // ✅ No existe, se crea nueva
            val nuevoId = if (obra.idObra.isEmpty()) obrasCollection.document().id else obra.idObra
            val nuevaObra = obra.copy(
                idObra = nuevoId,
                nombreObraLower = nombreNormalizado
            )
            obrasCollection.document(nuevoId).set(nuevaObra).await()
        } else {
            if (esActualizacion) {
                // 🛠 Si se está actualizando, sobreescribe la existente
                obrasCollection.document(obra.idObra).set(obra).await()
            } else {
                // ❌ Ya existe y no es actualización
                throw Exception("Ya existe una obra con este nombre.")
            }
        }
    }



    // 🔹 Buscar obra por nombre (ignorando mayúsculas/minúsculas)
    suspend fun buscarObra(nombreObra: String): Obra? {
        val nombreNormalizado = nombreObra.trim().lowercase()

        val query = obrasCollection
            .whereEqualTo("nombreObraLower", nombreNormalizado)
            .get()
            .await()

        return if (!query.isEmpty) query.documents[0].toObject(Obra::class.java) else null
    }


    // 🔹 Eliminar una obra
    suspend fun eliminarObra(idObra: String) {
        obrasCollection.document(idObra).delete().await()
    }

    // 🔹 Actualizar obra existente
    suspend fun actualizarObra(obra: Obra) {
        val db = FirebaseFirestore.getInstance()
        val obrasCollection = db.collection("obras")

        // Buscar el documento por idObra o nombreObraLower
        val querySnapshot = obrasCollection
            .whereEqualTo("idObra", obra.idObra)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            // Si existe, actualiza el documento
            val document = querySnapshot.documents.first()
            obrasCollection.document(document.id).set(obra).await()
        } else {
            // Si no existe, lo crea como respaldo
            obrasCollection.document(obra.idObra).set(obra).await()
        }
    }

}

