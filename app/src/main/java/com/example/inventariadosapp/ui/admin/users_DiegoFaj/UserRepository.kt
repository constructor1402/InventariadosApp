package com.example.inventariadosapp.ui.admin.users_DiegoFaj

import com.example.inventariadosapp.screens.admin.gestion.Obra
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collectionUsuarios = db.collection("usuarios")
    private val collectionEquipos = db.collection("equipos")
    private val collectionObras = db.collection("obras")

    // 🔹 Buscar usuario por correo
    suspend fun buscarUsuario(correo: String): UserUiState? {
        val query = collectionUsuarios
            .whereEqualTo("correoElectronico", correo)
            .get()
            .await()

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

    // 🔹 Guardar usuario
    suspend fun guardarUsuario(user: UserUiState) {
        val usuario = hashMapOf(
            "nombreCompleto" to user.nombre,
            "numeroCelular" to user.celular,
            "correoElectronico" to user.correo,
            "contrasena" to user.contrasena,
            "rolSeleccionado" to user.rol
        )
        collectionUsuarios.document(user.correo).set(usuario).await()
    }

    // 🔹 Eliminar usuario
    suspend fun eliminarUsuario(correo: String) {
        val query = collectionUsuarios.whereEqualTo("correoElectronico", correo).get().await()
        for (doc in query.documents) {
            collectionUsuarios.document(doc.id).delete().await()
        }
    }

    // 🔹 Obtener equipos filtrados
    suspend fun obtenerEquiposFiltrados(codigo: String, tipo: String): List<Equipo> {
        return try {
            var query: Query = collectionEquipos

            if (tipo.isNotBlank()) {
                query = query.whereEqualTo("tipo", tipo)
            }
            if (codigo.isNotBlank()) {
                query = query.whereEqualTo("serial", codigo)
            }
            
            query.get().await().toObjects(Equipo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    // 🔹 Obtener obras filtradas
    suspend fun obtenerObrasFiltradas(nombreObra: String): List<Obra> {
        return try {
            var query: Query = collectionObras

            if (nombreObra.isNotBlank()) {
                query = query.whereEqualTo("nombreObra", nombreObra)
            }

            query.get().await().toObjects(Obra::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    companion object
}
