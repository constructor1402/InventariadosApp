package com.example.inventariadosapp.admin.consulta.models

import com.google.firebase.Timestamp

/**
 * Clase de datos que mapea un informe generado.
 */
data class ReportItem(
    val id: String = "",
    val codigo: String? = null,
    val serial: String? = null, // <-- NUEVO: Para capturar el serial
    val descripcion: String? = null,
    val tipo: String? = null,
    val referencia: String? = null,
    val fechaCertificacion: String? = null, // <-- NUEVO: Parece ser String en Firestore
    // Campo usado por la colección "informes_generados"
    val fechaGeneracion: Timestamp? = null,
    // Campo usado por la colección "informes" (la otra)
    val fechaCreacion: Timestamp? = null,
    // Campos que no mapeamos en Firestore, sino que calculamos o usamos para mostrar
    val fechaReporte: Timestamp? = null, // Fecha unificada para ordenar y mostrar
    val fuenteColeccion: String = "" // Para saber de dónde viene
)

/**
 * Clase de datos para las métricas que se muestran en la pantalla de inicio.
 */
data class MetricData(
    val equiposDisponibles: Int = 0,
    val equiposEnUso: Int = 0
)