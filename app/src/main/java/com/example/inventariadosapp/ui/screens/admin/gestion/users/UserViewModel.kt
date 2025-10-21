package com.example.inventariadosapp.ui.screens.admin.gestion.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UserViewModel : ViewModel() {

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
            if (nombre.isBlank() || celular.isBlank() || correo.isBlank() ||
                contrasena.isBlank() || rol.isBlank()
            ) {
                mensajeEstado = "Por favor completa todos los campos ⚠️"
                return@launch
            }

            if (!correo.contains("@") || !correo.contains(".")) {
                mensajeEstado = "Correo electrónico inválido ❌"
                return@launch
            }

            val usuario = UserUiState(nombre, celular, correo, contrasena, rol)
            repository.guardarOActualizarUsuario(usuario)

            // 🧹 Limpia los campos después de guardar
            limpiarCampos()

            mensajeEstado = "Usuario guardado o actualizado correctamente ✅"
        }
    }


    fun buscarUsuario() {
        viewModelScope.launch {
            if (correo.isBlank()) {
                mensajeEstado = "Ingresa un correo para buscar ⚠️"
                return@launch
            }

            val user = repository.buscarUsuario(correo)
            if (user != null) {
                nombre = user.nombre
                celular = user.celular
                contrasena = user.contrasena
                rol = user.rol
                mensajeEstado = "Usuario encontrado con éxito ✅"
            } else {
                limpiarCampos()
                mensajeEstado = "Usuario no encontrado ❌"
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

data class UserUiState(
    val nombre: String = "",
    val celular: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val rol: String = ""
)


