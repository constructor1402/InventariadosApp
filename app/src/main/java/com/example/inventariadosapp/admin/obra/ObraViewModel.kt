package com.example.inventariadosapp.admin.obra

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventariadosapp.admin.obra.models.Obra

class ObraViewModel : ViewModel() {

    // 1. ESTADO DE LA UI
    var nombreObra by mutableStateOf("")
        private set
    var ubicacion by mutableStateOf("")
        private set
    var clienteNombre by mutableStateOf("")
        private set
    var clienteId by mutableStateOf("") // ID interno, no visible directamente

    // 2. ESTADO DE VALIDACIÓN (Errores)
    var nombreObraError by mutableStateOf(false)
        private set
    var ubicacionError by mutableStateOf(false)
        private set
    var mensajeStatus by mutableStateOf<String?>(null) // Para mensajes de éxito/error

    // 3. ACTUALIZADORES DE ESTADO
    fun updateNombreObra(newValue: String) {
        nombreObra = newValue
        nombreObraError = false // Limpiar error al escribir
    }

    fun updateUbicacion(newValue: String) {
        ubicacion = newValue
        ubicacionError = false // Limpiar error al escribir
    }

    // Funciones de ejemplo para simular la interacción con el cliente
    fun seleccionarCliente(id: String, nombre: String) {
        clienteId = id
        clienteNombre = nombre
    }

    // 4. LÓGICA DE NEGOCIO (CRUD)

    fun guardarObra() {
        // Validación: Revisa si los campos obligatorios están llenos
        if (nombreObra.isBlank()) {
            nombreObraError = true
            mensajeStatus = "⚠️ El Nombre de Obra es obligatorio."
            return
        }
        if (ubicacion.isBlank()) {
            ubicacionError = true
            mensajeStatus = "⚠️ La Ubicación es obligatoria."
            return
        }

        // Aquí iría la lógica para GUARDAR/ACTUALIZAR la obra en la base de datos
        val obraAGuardar = Obra(
            nombre = nombreObra,
            ubicacion = ubicacion,
            clienteId = clienteId,
            clienteNombre = clienteNombre
        )
        // Lógica de base de datos (Ej. Firebase.save(obraAGuardar) )

        // Simulación de éxito
        mensajeStatus = "✅ La obra ha sido guardada/modificada con éxito."
    }

    fun eliminarObra() {
        // Lógica para ELIMINAR la obra actual de la base de datos
        // ...

        // Simulación de éxito
        limpiarCampos()
        mensajeStatus = "❌ La obra ha sido eliminada con éxito."
    }

    fun buscarObra(nombre: String) {
        // Lógica para BUSCAR una obra y cargar sus datos en el estado
        // ...

        // Simulación de carga de datos
        if (nombre == "LA PALMA") {
            nombreObra = "LA PALMA"
            ubicacion = "CUNDINAMARCA-SESME"
            seleccionarCliente("ID_MARVAL", "MARVAL S.A.")
            mensajeStatus = "Obra cargada."
        } else {
            mensajeStatus = "Obra no encontrada."
        }
    }

    fun limpiarCampos() {
        nombreObra = ""
        ubicacion = ""
        clienteNombre = ""
        clienteId = ""
        nombreObraError = false
        ubicacionError = false
        mensajeStatus = null
    }
}