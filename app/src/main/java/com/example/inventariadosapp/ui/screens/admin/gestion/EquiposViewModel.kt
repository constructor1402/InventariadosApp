package com.example.inventariadosapp.ui.screens.admin.gestion

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*
import com.google.firebase.FirebaseStorage
import com.google.firebase.storage.FirebaseStorage
*/



data class Equipo(
    val serial: String = "",
    val referencia: String = "",
    val tipo: String = "",
    val fechaCertificacion: String = "",
    val certificadoUrl: String = ""
)

class EquiposViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    // private val dbEspacio = FirebaseStorage.getInstance()

    var serial by mutableStateOf("")
    var referencia by mutableStateOf("")
    var fecha by mutableStateOf("")
    var tipo by mutableStateOf("")
    var certificadoUrl: Uri? = null

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje


    fun guardarEquipo(){
        if(serial.isBlank() || referencia.isBlank() || tipo.isBlank()){
            _mensaje.value = "Por fabor completa todos los campos."
            return
        }

        viewModelScope.launch {
            try{
                // val certificadoUrl = certificadoUrl?.let { subirCertificadoFirebase(it) } ?: ""

                val equipo = hashMapOf(
                    "serial" to serial,
                    "referencia" to referencia,
                    "tipo" to tipo,
                    "fechaCertificacion" to fecha,
                    "certificadoUrl" to certificadoUrl
                )

                db.collection("equipos").document(serial).set(equipo).await()
                _mensaje.value = "Equipo guardado correctamente"

                limpiarCampos()
            }catch (e: Exception){
                _mensaje.value = "Error al guardar: ${e.message}"
            }
        }
    }

    fun buscarEquipo(){
        if (serial.isBlank()) {
            _mensaje.value = "Ingrese un serial para buscar."
            return
        }
        viewModelScope.launch {
            try {
                val doc = db.collection("equipos").document(serial).get().await()
                if (doc.exists()) {
                    referencia = doc.getString("referencia") ?: ""
                    tipo = doc.getString("tipo") ?: ""
                    fecha = doc.getString("fechaCertificacion") ?: ""
                    _mensaje.value = "Equipo encontrado ✅"
                } else {
                    _mensaje.value = "No se encontró el equipo."
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al buscar: ${e.message}"
            }
        }
    }

    fun eliminarEquipo(){
        if (serial.isBlank()) {
            _mensaje.value = "Ingrese el serial del equipo a eliminar."
            return
        }

        viewModelScope.launch {
            try {
                db.collection("equipos").document(serial).delete().await()
                _mensaje.value = "Equipo eliminado correctamente 🗑️"
                limpiarCampos()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar: ${e.message}"
            }
        }
    }
    fun subirCertificado(uri: Uri) {
        certificadoUrl = uri
        _mensaje.value = "Archivo seleccionado: ${uri.lastPathSegment}"
    }

    /*
    private suspend fun subirCertificadoFirebase(uri: Uri): String {
        val ref = dbEspacio.reference.child("certificados/${uri.lastPathSegment}")
        ref.putFile(uri)
        return ref.downloadUrl.toString()
    }

     */

    // --- LIMPIAR CAMPOS ---
    private fun limpiarCampos() {
        serial = ""
        referencia = ""
        tipo = ""
        fecha = ""
        certificadoUrl = null
    }
}