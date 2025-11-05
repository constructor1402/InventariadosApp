package com.example.inventariadosapp.ui.screens.admin.gestion.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UserViewModel : ViewModel() {

    var idUsuario by mutableStateOf("")
    var nombre by mutableStateOf("")
    var celular by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var rol by mutableStateOf("")
    var mensajeEstado by mutableStateOf<String?>(null)

    private val repository = UserRepository()

    fun onFieldChange(
        nombre: String? = null,
        celular: String? = null,
        correo: String? = null,
        contrasena: String? = null,
        rol: String? = null
    ) {
        nombre?.let { this.nombre = it }
        celular?.let { this.celular = it }
        correo?.let { this.correo = it }
        contrasena?.let { this.contrasena = it }
        rol?.let { this.rol = it }
    }

    fun guardarUsuario() {
        viewModelScope.launch {
            try {
                if (nombre.isBlank() || celular.isBlank() || correo.isBlank() ||
                    contrasena.isBlank() || rol.isBlank()
                ) {
                    mensajeEstado = "⚠️ Por favor completa todos los campos"
                    return@launch
                }

                if (!correo.contains("@") || !correo.contains(".")) {
                    mensajeEstado = "❌ Correo electrónico inválido"
                    return@launch
                }

                val usuario = UserUiState(
                    idUsuario = idUsuario,
                    nombre = nombre,
                    celular = celular,
                    correo = correo,
                    contrasena = contrasena,
                    rol = rol
                )

                repository.guardarOActualizarUsuario(usuario)

                limpiarCampos()
                mensajeEstado = "✅ Usuario guardado correctamente"

            } catch (e: Exception) {
                mensajeEstado = e.message ?: "❌ Error al guardar el usuario"
            }
        }
    }



    fun buscarUsuarioPorCorreo() {

        viewModelScope.launch {
            try {
                val resultado = repository.buscarUsuario(correo)
                if (resultado != null) {
                    // ✅ Asigna los datos encontrados
                    idUsuario = resultado.idUsuario
                    nombre = resultado.nombre
                    celular = resultado.celular
                    correo = resultado.correo
                    contrasena = resultado.contrasena
                    rol = resultado.rol

                    mensajeEstado = "✅ Usuario encontrado"
                } else {
                    mensajeEstado = "❌ No se encontró ningún usuario con ese correo"
                }
            } catch (e: Exception) {
                mensajeEstado = "⚠️ Error al buscar: ${e.message}"
            }
        }
    }


    fun eliminarUsuario() {
        viewModelScope.launch {
            if (correo.isBlank()) {
                mensajeEstado = "Debes ingresar un correo para eliminar ⚠️"
                return@launch
            }

            repository.eliminarUsuario(correo)
            limpiarCampos()
            mensajeEstado = "Usuario eliminado correctamente 🗑️"
        }
    }

    fun limpiarCampos() {
        nombre = ""
        celular = ""
        correo = ""
        contrasena = ""
        rol = ""
    }
}



