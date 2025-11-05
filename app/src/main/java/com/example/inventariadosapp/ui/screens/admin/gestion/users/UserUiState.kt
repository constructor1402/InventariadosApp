package com.example.inventariadosapp.ui.screens.admin.gestion.users


data class UserUiState(
    val idUsuario: String = "",
    val nombre: String = "",
    val celular: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val rol: String = ""
)
