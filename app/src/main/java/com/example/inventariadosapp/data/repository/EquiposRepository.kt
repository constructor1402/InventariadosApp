package com.example.inventariadosapp.data.repository

import com.example.inventariadosapp.domain.model.Equipo
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import android.util.Log


class EquiposRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val equiposCollection = db.collection("equipos")

    // 🔹 Guardar o actualizar un equipo (verificando duplicados)
    suspend fun guardarEquipo(equipo: Equipo): String {
        val docRef = equiposCollection.document(equipo.serial)
        val snapshot = docRef.get().await()

        return if (snapshot.exists()) {
            // Ya hay un equipo con ese serial
            "EXISTE"
        } else {
            // No existe, así que lo crea
            docRef.set(equipo).await()
            "NUEVO"
        }
    }

    // 🔹 Buscar un equipo por serial
    suspend fun buscarEquipo(serial: String): Equipo? {
        val snapshot = equiposCollection.document(serial).get().await()
        return snapshot.toObject(Equipo::class.java)
    }

    // 🔹 Actualizar equipo existente (sin verificar duplicados)
    suspend fun actualizarEquipo(equipo: Equipo) {
        equiposCollection.document(equipo.serial).set(equipo).await()
    }

    // 🔹 Eliminar equipo y su certificado del Storage
    suspend fun eliminarEquipo(serial: String, certificadoUrl: String?) {
        try {
            // 1️⃣ Eliminar el documento en Firestore
            equiposCollection.document(serial).delete().await()

            // 2️⃣ Si hay un certificado en Storage, también se elimina
            if (!certificadoUrl.isNullOrEmpty()) {
                val storage = FirebaseStorage.getInstance()
                val ref = storage.getReferenceFromUrl(certificadoUrl)
                ref.delete().await()
            }

        } catch (e: Exception) {
            throw Exception("Error al eliminar equipo: ${e.message}")
        }
    }


    // 🔹 Subir certificado (reemplaza si ya existe)
    suspend fun subirCertificado(serial: String, bytes: ByteArray, certificadoUrlAnterior: String?): String {
        val storage = FirebaseStorage.getInstance()

        // 🔸 Si ya hay un certificado anterior, intenta eliminarlo antes de subir el nuevo
        if (!certificadoUrlAnterior.isNullOrEmpty()) {
            try {
                val oldRef = storage.getReferenceFromUrl(certificadoUrlAnterior)
                oldRef.delete().await()
            } catch (e: Exception) {
                Log.w("FirebaseStorage", "No se pudo eliminar el certificado anterior: ${e.message}")
            }
        }

        // 🔸 Subir el nuevo certificado (uno por equipo)
        val ref = storage.reference.child("certificados/$serial.pdf")
        ref.putBytes(bytes).await()

        // 🔸 Obtener el nuevo enlace de descarga
        val nuevaUrl = ref.downloadUrl.await().toString()

        // 🔸 Guardar o actualizar el registro del certificado en Firestore
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("certificados").whereEqualTo("serial", serial).get().await()

        if (!query.isEmpty) {
            val docId = query.documents[0].id
            db.collection("certificados").document(docId).update(
                mapOf(
                    "url" to nuevaUrl,
                    "fecha" to FieldValue.serverTimestamp()
                )
            ).await()
        } else {
            db.collection("certificados").add(
                mapOf(
                    "serial" to serial,
                    "url" to nuevaUrl,
                    "fecha" to FieldValue.serverTimestamp()
                )
            ).await()
        }

        return nuevaUrl
    }


    // 🔹 Obtener todos los tipos de equipo (desde colección tipos_equipos)
    suspend fun obtenerTiposEquipos(): List<String> {
        val tiposSnapshot = db.collection("tipos_equipos").get().await()
        return tiposSnapshot.documents.mapNotNull { it.getString("nombre") }
    }

    // 🔹 Agregar un nuevo tipo de equipo (evita duplicados)
    suspend fun agregarNuevoTipo(nombre: String) {
        val tiposCollection = db.collection("tipos_equipos")

        // Verifica si ya existe un tipo con ese nombre (ignorando mayúsculas/minúsculas)
        val existe = tiposCollection
            .whereEqualTo("nombre", nombre)
            .get()
            .await()

        if (existe.isEmpty) {
            val data = hashMapOf("nombre" to nombre)
            tiposCollection.add(data).await()
        }
    }

}
