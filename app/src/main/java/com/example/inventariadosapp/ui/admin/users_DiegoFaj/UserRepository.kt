package com.example.inventariadosapp.ui.admin.users_DiegoFaj

import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

    private val collectionEquipo = db.collection("Equipos")

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

    suspend fun obtenerEquiposFiltrados(codigo: String, estado: String): List<Equipo> {
        val query = collectionEquipo

        var queryFinal = query
        if (codigo.isNotBlank()) {
            queryFinal = queryFinal.whereEqualTo("codigo", codigo) as CollectionReference
        }
        if (estado.isNotBlank()) {
            queryFinal = queryFinal.whereEqualTo("estado", estado) as CollectionReference
        }

        return queryFinal.get().await().toObjects(Equipo::class.java)
    }

    companion object

}


