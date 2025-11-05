package com.example.inventariadosapp.ui.screens.admin.gestion.obras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.data.repository.ObraRepository
import com.example.inventariadosapp.domain.model.Obra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ObraViewModel : ViewModel() {

    private val repository = ObraRepository()

    // Campos del formulario
    val nombreObra = MutableStateFlow("")
    val ubicacion = MutableStateFlow("")
    val clienteNombre = MutableStateFlow("")

    // Mensaje de estado para mostrar en pantalla
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    // 🔹 Actualiza campos (se conectan con los CustomTextField)
    fun updateNombreObra(nuevo: String) {
        nombreObra.value = nuevo
    }

    fun updateUbicacion(nueva: String) {
        ubicacion.value = nueva
    }

    fun updateCliente(nuevo: String) {
        clienteNombre.value = nuevo
    }

    // 🔹 Limpia los campos del formulario
    private fun limpiarCampos() {
        nombreObra.value = ""
        ubicacion.value = ""
        clienteNombre.value = ""
    }

    // 🔹 Buscar obra por nombre
    fun buscarObra() {
        viewModelScope.launch {
            try {
                val obra = repository.buscarObra(nombreObra.value)
                if (obra != null) {
                    nombreObra.value = obra.nombreObra
                    ubicacion.value = obra.ubicacion
                    clienteNombre.value = obra.clienteNombre
                    _mensaje.value = "🔍 Obra encontrada"
                } else {
                    _mensaje.value = "⚠️ No se encontró la obra"
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al buscar: ${e.message}"
            }
        }
    }

    // 🔹 Guardar o actualizar obra (con validación de duplicado)
    fun guardarObra() {
        viewModelScope.launch {
            try {
                val nombreNormalizado = nombreObra.value.trim().lowercase()
                val obraExistente = repository.buscarObra(nombreNormalizado)

                // Si ya existe una obra con el mismo nombre, mostrar mensaje y salir
                if (obraExistente != null) {
                    _mensaje.value = "⚠️ Ya existe una obra con este nombre. Usa 'Buscar' para actualizarla."
                    return@launch
                }

                // Si no existe, se crea nueva
                val nuevaObra = Obra(
                    idObra = "", // Firebase lo generará automáticamente
                    nombreObra = nombreObra.value,
                    nombreObraLower = nombreNormalizado,
                    ubicacion = ubicacion.value,
                    clienteNombre = clienteNombre.value
                )

                repository.guardarObra(nuevaObra)
                _mensaje.value = "✅ Obra guardada correctamente"

                limpiarCampos()
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al guardar: ${e.message}"
            }
        }
    }


    // 🔹 Eliminar obra
    fun eliminarObra() {
        viewModelScope.launch {
            try {
                val obra = repository.buscarObra(nombreObra.value)
                if (obra != null) {
                    repository.eliminarObra(obra.idObra)
                    limpiarCampos()
                    _mensaje.value = "🗑️ Obra eliminada correctamente"
                } else {
                    _mensaje.value = "⚠️ No se encontró la obra a eliminar"
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al eliminar: ${e.message}"
            }
        }
    }
}
