package com.example.inventariadosapp.ui.screens.admin.informes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.domain.model.Obra
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InformeObrasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _obras = MutableStateFlow<List<Obra>>(emptyList())
    val obras: StateFlow<List<Obra>> = _obras.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        cargarObras()
    }

    private fun cargarObras() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("obras").get().await()
                _obras.value = snapshot.toObjects(Obra::class.java)
            } catch (e: Exception) {
                _obras.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }



    suspend fun guardarInformeEnFirebase(filePath: String, tipo: String, userCorreo: String) {
        val storage = com.google.firebase.storage.FirebaseStorage.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        try {
            val file = java.io.File(filePath)
            val fileUri = android.net.Uri.fromFile(file)
            val storageRef = storage.reference.child("informes/$userCorreo/${file.name}")

            // Subir PDF al Storage
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Registrar el informe en Firestore
            val informeData = hashMapOf(
                "correoUsuario" to userCorreo,
                "tipo" to tipo,
                "fecha" to com.google.firebase.Timestamp.now(),
                "nombreArchivo" to file.name,
                "url" to downloadUrl
            )

            db.collection("informes").add(informeData).await()
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error guardando informe", e)
        }
    }

    suspend fun generarInformePDFobras(
        obras: List<com.example.inventariadosapp.domain.model.Obra>,
        userCorreo: String
    ): String {
        return try {
            val fileName = "Informe_Obras_${System.currentTimeMillis()}.pdf"
            val downloads = android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_DOWNLOADS
            )
            val file = java.io.File(downloads, fileName)
            file.writeText("Informe generado por $userCorreo con ${obras.size} obras registradas.")
            file.absolutePath
        } catch (e: Exception) {
            "Error al generar PDF: ${e.message}"
        }
    }


}
