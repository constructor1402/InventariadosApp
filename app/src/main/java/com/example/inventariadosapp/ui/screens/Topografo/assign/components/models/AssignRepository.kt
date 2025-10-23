package com.example.inventariadosapp.ui.screens.Topografo.assign.models

import com.google.firebase.firestore.FirebaseFirestore

class AssignRepository {

    private val db = FirebaseFirestore.getInstance()

    fun guardarAsignacion(
        serial: String,
        referencia: String,
        tipo: String,
        obra: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val data = hashMapOf(
            "serial" to serial,
            "referencia" to referencia,
            "tipo" to tipo,
            "obra" to obra,
            "fecha" to System.currentTimeMillis()
        )

        db.collection("asignaciones")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error al guardar") }
    }
}
