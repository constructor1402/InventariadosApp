package com.example.inventariadosapp.ui.screens.admin.gestion.equipos

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

    init {
        obtenerTipos()
    }

    // 🔹 Actualiza los valores desde la UI
    fun onSerialChange(value: String) { serial.value = value }
    fun onReferenciaChange(value: String) { referencia.value = value }
    fun onDescripcionChange(value: String) { descripcion.value = value }
    fun onTipoChange(value: String) { tipo.value = value }
    fun onFechaChange(value: String) { fechaCertificacion.value = value }

    // 🔹 Guarda o actualiza un equipo
    fun guardarEquipo() {
        viewModelScope.launch {
            try {
                val equipo = Equipo(
                    serial = serial.value,
                    referencia = referencia.value,
                    descripcion = descripcion.value,
                    tipo = tipo.value,
                    fechaCertificacion = fechaCertificacion.value,
                    certificadoUrl = certificadoUrl.value
                )
                repository.guardarEquipo(equipo)
                _mensaje.value = "✅ Equipo guardado correctamente"
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
                    _mensaje.value = "🔍 Equipo encontrado"
                } else {
                    _mensaje.value = "⚠️ No se encontró el equipo"
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al buscar: ${e.message}"
            }
        }
    }

    // 🔹 Eliminar equipo
    fun eliminarEquipo() {
        viewModelScope.launch {
            try {
                repository.eliminarEquipo(serial.value)
                limpiarCampos()
                _mensaje.value = "🗑️ Equipo eliminado correctamente"
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al eliminar: ${e.message}"
            }
        }
    }

    // 🔹 Subir certificado
    fun subirCertificado(bytes: ByteArray) {
        viewModelScope.launch {
            try {
                val url = repository.subirCertificado(serial.value, bytes)
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
                repository.agregarNuevoTipo(nombre)
                obtenerTipos()
                _mensaje.value = "🆕 Tipo agregado correctamente"
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al agregar tipo: ${e.message}"
            }
        }
    }

    // 🔹 Limpia los campos del formulario
    private fun limpiarCampos() {
        serial.value = ""
        referencia.value = ""
        descripcion.value = ""
        tipo.value = ""
        fechaCertificacion.value = ""
        certificadoUrl.value = ""
    }
}





