package com.example.inventariadosapp.ui.admin.users_DiegoFaj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class UserUiState(
    val nombre: String = "",
    val celular: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val rol: String = "",
    val mensaje: String = ""
)

class UserViewModel : ViewModel() {
    var mensajeEstado by mutableStateOf<String?>(null)


    private val repository = UserRepository()

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    fun onFieldChange(nombre: String? = null, celular: String? = null, correo: String? = null, contrasena: String? = null, rol: String? = null) {
        _uiState.value = _uiState.value.copy(
            nombre = nombre ?: _uiState.value.nombre,
            celular = celular ?: _uiState.value.celular,
            correo = correo ?: _uiState.value.correo,
            contrasena = contrasena ?: _uiState.value.contrasena,
            rol = rol ?: _uiState.value.rol
        )
    }

    fun buscarUsuario() {
        viewModelScope.launch {
            val user = repository.buscarUsuario(_uiState.value.correo)
            if (user != null) {
                _uiState.value = user
                mensajeEstado = "Usuario encontrado con éxito ✅"
            } else {
                mensajeEstado = "Usuario no encontrado ❌"
            }
        }
    }

    fun guardarUsuario() {
        viewModelScope.launch {
            val usuario = _uiState.value

            // 🔹 Validaciones básicas
            when {
                usuario.nombre.isBlank() || usuario.correo.isBlank() ||
                        usuario.contrasena.isBlank() || usuario.rol.isBlank() -> {
                    mensajeEstado = "Por favor completa todos los campos ⚠️"
                }

                !usuario.correo.contains("@") || !usuario.correo.contains(".") -> {
                    mensajeEstado = "Correo electrónico inválido ❌"
                }

                else -> {
                    // 🔹 Si todo está bien, guarda
                    repository.guardarUsuario(usuario)
                    mensajeEstado = "Usuario guardado correctamente ✅"
                }
            }
        }
    }


    fun eliminarUsuario() {
        viewModelScope.launch {
            repository.eliminarUsuario(_uiState.value.correo)
            _uiState.value = UserUiState()
            mensajeEstado = "Usuario eliminado correctamente 🗑️"
        }
    }
}



