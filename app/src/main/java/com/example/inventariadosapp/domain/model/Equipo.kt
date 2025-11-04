package com.example.inventariadosapp.domain.model

data class Equipo(
    val serial: String = "",
    val referencia: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val fechaCertificacion: String = "",
    val certificadoUrl: String = "",
    val estado: String = "Disponible"
)
