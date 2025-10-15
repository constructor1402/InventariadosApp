package com.example.inventariadosapp.admin.obra.models

data class Obra(
    val id: String = "",
    val nombre: String = "",
    val ubicacion: String = "",
    val clienteId: String = "",
    val clienteNombre: String = "" // Para mostrar el nombre del cliente asociado
)