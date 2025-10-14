package com.example.inventariadosapp.ui.admin.users_DiegoFaj

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

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

    suspend fun guardarUsuario(user: UserUiState) {
        val usuario = hashMapOf(
            "nombreCompleto" to user.nombre,
            "numeroCelular" to user.celular,
            "correoElectronico" to user.correo,
            "contrasena" to user.contrasena,
            "rolSeleccionado" to user.rol
        )
        collection.document(user.correo).set(usuario).await()
    }

    suspend fun eliminarUsuario(correo: String) {
        val query = collection.whereEqualTo("correoElectronico", correo).get().await()
        for (doc in query.documents) {
            collection.document(doc.id).delete().await()
        }
    }
}

