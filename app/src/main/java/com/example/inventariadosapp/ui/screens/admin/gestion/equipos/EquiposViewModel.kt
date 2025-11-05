package com.example.inventariadosapp.ui.screens.admin.gestion.equipos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.data.repository.EquiposRepository
import com.example.inventariadosapp.domain.model.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquiposViewModel : ViewModel() {

    private val repository = EquiposRepository()

    // Estados de los campos de la UI
    var serial = MutableStateFlow("")
        private set
    var referencia = MutableStateFlow("")
        private set
    var descripcion = MutableStateFlow("")
        private set
    var tipo = MutableStateFlow("")
        private set
    var fechaCertificacion = MutableStateFlow("")
        private set
    var certificadoUrl = MutableStateFlow("")
        private set


    // Listado de tipos disponibles
    private val _tiposEquipos = MutableStateFlow<List<String>>(emptyList())
    val tiposEquipos = _tiposEquipos.asStateFlow()

    // Mensajes para mostrar en pantalla
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    var isEditing = mutableStateOf(false)


    init {
        obtenerTipos()
    }

    // 🔹 Actualiza los valores desde la UI
    fun onSerialChange(value: String) { serial.value = value }
    fun onReferenciaChange(value: String) { referencia.value = value }
    fun onDescripcionChange(value: String) { descripcion.value = value }
    fun onTipoChange(value: String) { tipo.value = value }
    fun onFechaChange(value: String) { fechaCertificacion.value = value }

    // 🔹 Guarda un equipo (verifica si ya existe antes de crear)
    fun guardarEquipo() {
        viewModelScope.launch {
            try {
                val existe = repository.buscarEquipo(serial.value) != null

                val equipo = Equipo(
                    serial = serial.value,
                    referencia = referencia.value,
                    descripcion = descripcion.value,
                    tipo = tipo.value,
                    fechaCertificacion = fechaCertificacion.value,
                    certificadoUrl = certificadoUrl.value
                )

                if (existe) {
                    repository.actualizarEquipo(equipo)
                    _mensaje.value = "🔄 Equipo actualizado correctamente"
                    limpiarCampos()
                } else {
                    repository.guardarEquipo(equipo)
                    _mensaje.value = "✅ Equipo guardado correctamente"
                    limpiarCampos()
                }

            } catch (e: Exception) {
                _mensaje.value = "❌ Error al guardar: ${e.message}"
            }
        }
    }


    // 🔹 Buscar equipo por serial
    fun buscarEquipo() {
        viewModelScope.launch {
            try {
                val equipo = repository.buscarEquipo(serial.value)
                if (equipo != null) {
                    referencia.value = equipo.referencia
                    descripcion.value = equipo.descripcion
                    tipo.value = equipo.tipo
                    fechaCertificacion.value = equipo.fechaCertificacion
                    certificadoUrl.value = equipo.certificadoUrl

                    // ✅ Activar modo edición
                    isEditing.value = true

                    _mensaje.value = "🔍 Equipo encontrado"
                } else {
                    _mensaje.value = "⚠️ No se encontró el equipo"

                    // 🔹 Si no existe, aseguramos que no esté en modo edición
                    isEditing.value = false
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al buscar: ${e.message}"
                isEditing.value = false
            }
        }
    }


    // 🔹 Eliminar equipo y limpiar campos
    fun eliminarEquipo() {
        viewModelScope.launch {
            try {
                repository.eliminarEquipo(serial.value, certificadoUrl.value)
                limpiarCampos()
                _mensaje.value = "🗑️ Equipo y certificado eliminados correctamente"
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al eliminar: ${e.message}"
            }
        }
    }

    // 🔹 Subir certificado (reemplaza si ya existe)
    fun subirCertificado(bytes: ByteArray) {
        viewModelScope.launch {
            try {
                // Pasamos también la URL anterior al repositorio
                val url = repository.subirCertificado(serial.value, bytes, certificadoUrl.value)
                certificadoUrl.value = url
                _mensaje.value = "📄 Certificado subido correctamente"
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al subir archivo: ${e.message}"
            }
        }
    }



    // 🔹 Obtener tipos desde Firebase
    private fun obtenerTipos() {
        viewModelScope.launch {
            try {
                _tiposEquipos.value = repository.obtenerTiposEquipos()
            } catch (e: Exception) {
                _mensaje.value = "⚠️ Error al cargar tipos"
            }
        }
    }

    // 🔹 Agregar un nuevo tipo
    fun agregarNuevoTipo(nombre: String) {
        viewModelScope.launch {
            try {
                if (nombre.isBlank()) {
                    _mensaje.value = "⚠️ Escribe un nombre válido"
                    return@launch
                }

                repository.agregarNuevoTipo(nombre)
                obtenerTipos() // 🔄 Actualiza la lista en pantalla
                tipo.value = nombre // Asigna el nuevo tipo seleccionado automáticamente
                _mensaje.value = "✅ Tipo agregado correctamente"

            } catch (e: Exception) {
                _mensaje.value = "❌ Error al agregar tipo: ${e.message}"
            }
        }
    }


    // 🔹 Limpia los campos del formulario
    fun limpiarCampos() {
        serial.value = ""
        referencia.value = ""
        descripcion.value = ""
        tipo.value = ""
        fechaCertificacion.value = ""
        certificadoUrl.value = ""
    }
}





