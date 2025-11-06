package com.example.inventariadosapp.utils

data class UsuarioSesion(
    val id: String = "",
    val nombreCompleto: String = "",
    val correoElectronico: String = "",
    val rol: String = ""
)

object SesionUsuario {
    var usuarioActual: UsuarioSesion? = null
}

