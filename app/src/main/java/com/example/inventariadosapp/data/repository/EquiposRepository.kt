package com.example.inventariadosapp.data.repository

import com.example.inventariadosapp.domain.model.Equipo
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EquiposRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val equiposCollection = db.collection("equipos")

    // 🔹 Guardar o actualizar un equipo
    suspend fun guardarEquipo(equipo: Equipo) {
        equiposCollection.document(equipo.serial).set(equipo).await()
    }

    // 🔹 Buscar un equipo por serial
    suspend fun buscarEquipo(serial: String): Equipo? {
        val snapshot = equiposCollection.document(serial).get().await()
        return snapshot.toObject(Equipo::class.java)
    }

    // 🔹 Eliminar un equipo
    suspend fun eliminarEquipo(serial: String) {
        equiposCollection.document(serial).delete().await()
    }

    // 🔹 Subir certificado (PDF o imagen) al Storage
    suspend fun subirCertificado(serial: String, bytes: ByteArray): String {
        val nombreArchivo = "${serial}_${UUID.randomUUID()}.pdf"
        val ref = storage.reference.child("certificados/$nombreArchivo")

        // Subir los bytes al Storage
        ref.putBytes(bytes).await()

        // Obtener y devolver el enlace de descarga
        return ref.downloadUrl.await().toString()

        val db = FirebaseFirestore.getInstance()
        db.collection("certificados").add(
            mapOf(
                "serial" to serial,
                "url" to ref.downloadUrl.await().toString(),
                "fecha" to FieldValue.serverTimestamp()
            )
        )

    }

    // 🔹 Obtener todos los tipos de equipo (desde colección tipos_equipos)
    suspend fun obtenerTiposEquipos(): List<String> {
        val tiposSnapshot = db.collection("tipos_equipos").get().await()
        return tiposSnapshot.documents.mapNotNull { it.getString("nombre") }
    }

    // 🔹 Agregar un nuevo tipo de equipo
    suspend fun agregarNuevoTipo(nombre: String) {
        val data = hashMapOf("nombre" to nombre)
        db.collection("tipos_equipos").add(data).await()
    }
}
