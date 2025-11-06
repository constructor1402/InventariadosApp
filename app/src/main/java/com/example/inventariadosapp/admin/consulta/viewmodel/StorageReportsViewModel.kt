package com.example.inventariadosapp.admin.consulta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StorageReportsViewModel : ViewModel() {

    private val _carpetas = MutableStateFlow<List<String>>(emptyList())
    val carpetas: StateFlow<List<String>> = _carpetas

    private val _archivos = MutableStateFlow<List<String>>(emptyList())
    val archivos: StateFlow<List<String>> = _archivos

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun cargarContenido(folderPath: String, subcarpetaActual: String?) {
        viewModelScope.launch {
            _loading.value = true
            val storage = FirebaseStorage.getInstance()
            val ruta = if (subcarpetaActual == null) folderPath else "$folderPath/$subcarpetaActual"
            val listRef = storage.reference.child(ruta)

            listRef.listAll()
                .addOnSuccessListener { result ->
                    when {
                        folderPath == "informes_asignaciones" -> {
                            // 🔹 Solo archivos directos (PDFs)
                            _carpetas.value = emptyList()
                            _archivos.value = result.items.map { it.name }
                        }

                        folderPath == "informes" && subcarpetaActual == null -> {
                            // 🔹 Listar subcarpetas (usuarios dentro de informes/)
                            _carpetas.value = result.prefixes.map { it.name }
                            _archivos.value = emptyList()
                        }

                        folderPath == "informes" && subcarpetaActual != null -> {
                            // 🔹 Dentro de la carpeta de un usuario → mostrar archivos PDF
                            _carpetas.value = emptyList()
                            _archivos.value = result.items.map { it.name }
                        }

                        else -> {
                            _carpetas.value = emptyList()
                            _archivos.value = emptyList()
                        }
                    }

                    _loading.value = false
                }
                .addOnFailureListener { e ->
                    _loading.value = false
                    println("Error al listar archivos: ${e.message}")
                }
        }
    }


}
