package com.example.inventariadosapp.ui.screens.admin.gestion.users

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")


    // 💾 Crear o actualizar usuario (sin duplicar)
    suspend fun guardarOActualizarUsuario(user: UserUiState) {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("usuarios")

        val nombre = user.nombre.trim()
        val celular = user.celular.trim()
        val correo = user.correo.trim().lowercase()
        val contrasena = user.contrasena.trim()
        val rol = user.rol.trim()

        // Si el usuario tiene idUsuario, significa que fue buscado antes → actualizar
        if (user.idUsuario.isNotBlank()) {
            val datosActualizados = mapOf(
                "nombreCompleto" to nombre,
                "numeroCelular" to celular,
                "correoElectronico" to correo,
                "contrasena" to contrasena,
                "rolSeleccionado" to rol,
                "fechaRegistro" to com.google.firebase.Timestamp.now()
            )
            collection.document(user.idUsuario).update(datosActualizados).await()
            return
        }

        // Si no tiene idUsuario, es un nuevo registro → validar duplicados
        val queryCorreo = collection.whereEqualTo("correoElectronico", correo).get().await()
        val queryNombre = collection.whereEqualTo("nombreCompleto", nombre).get().await()

        if (!queryCorreo.isEmpty || !queryNombre.isEmpty) {
            throw Exception("⚠️ Ya existe un usuario con ese nombre o correo.")
        }

        // Crear nuevo documento con ID automático
        val nuevoDoc = collection.document()
        val datosUsuario = mapOf(
            "idUsuario" to nuevoDoc.id,
            "nombreCompleto" to nombre,
            "numeroCelular" to celular,
            "correoElectronico" to correo,
            "contrasena" to contrasena,
            "rolSeleccionado" to rol,
            "fechaRegistro" to com.google.firebase.Timestamp.now()
        )

        nuevoDoc.set(datosUsuario).await()
    }


    // 🔍 Buscar usuario por correo
    suspend fun buscarUsuario(correo: String): UserUiState? {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("usuarios")

        // 🔹 Buscar usuario por correo (en minúsculas para evitar errores)
        val query = collection.whereEqualTo("correoElectronico", correo.trim().lowercase()).get().await()
        if (query.isEmpty) return null

        val doc = query.documents.first()

        // 🔹 Retornar todos los datos del usuario encontrado
        return UserUiState(
            idUsuario = doc.getString("idUsuario") ?: doc.id, // usa el campo si existe, o el ID del documento si no
            nombre = doc.getString("nombreCompleto") ?: "",
            celular = doc.getString("numeroCelular") ?: "",
            correo = doc.getString("correoElectronico") ?: "",
            contrasena = doc.getString("contrasena") ?: "",
            rol = doc.getString("rolSeleccionado") ?: ""
        )
    }



    // 🗑️ Eliminar usuario por correo
    suspend fun eliminarUsuario(correo: String) {
        val query = collection.whereEqualTo("correoElectronico", correo.trim().lowercase()).get().await()
        for (doc in query.documents) {
            collection.document(doc.id).delete().await()
        }
    }
}
