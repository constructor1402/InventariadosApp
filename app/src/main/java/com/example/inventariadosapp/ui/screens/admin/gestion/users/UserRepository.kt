package com.example.inventariadosapp.ui.screens.admin.gestion.users

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

    // 🔍 Buscar usuario por correo
    suspend fun buscarUsuario(correo: String): UserUiState? {
        val query = collection.whereEqualTo("correoElectronico", correo).get().await()
        if (query.isEmpty) return null

        val doc = query.documents.first()
        return UserUiState(
            nombre = doc.getString("nombreCompleto") ?: "",
            celular = doc.getString("numeroCelular") ?: "",
            correo = doc.getString("correoElectronico") ?: "",
            contrasena = doc.getString("contrasena") ?: "",
            rol = doc.getString("rolSeleccionado") ?: ""
        )
    }

    // 💾 Crear o actualizar usuario
    suspend fun guardarOActualizarUsuario(user: UserUiState) {
        val query = collection.whereEqualTo("correoElectronico", user.correo).get().await()

        val usuario = hashMapOf(
            "nombreCompleto" to user.nombre,
            "numeroCelular" to user.celular,
            "correoElectronico" to user.correo,
            "contrasena" to user.contrasena,
            "rolSeleccionado" to user.rol
        )

        if (query.isEmpty) {
            // 🆕 Nuevo usuario
            collection.document(user.correo).set(usuario).await()
        } else {
            // 🔁 Actualización de datos
            val docId = query.documents.first().id
            collection.document(docId).update(usuario as Map<String, Any>).await()
        }
    }

    // 🗑️ Eliminar usuario
    suspend fun eliminarUsuario(correo: String) {
        val query = collection.whereEqualTo("correoElectronico", correo).get().await()
        for (doc in query.documents) {
            collection.document(doc.id).delete().await()
        }
    }
}
