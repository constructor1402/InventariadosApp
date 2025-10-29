package com.example.inventariadosapp.ui.screens.admin

import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.screens.admin.gestion.Obra
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserRepository
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream


class InformeEquiposViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val repository = UserRepository()

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    private val _obras = MutableStateFlow<List<Obra>>(emptyList())

    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()
    val obras: StateFlow<List<Obra>> = _obras.asStateFlow()

    init {
        viewModelScope.launch {
            cargarEquiposSuspend()
        }
    }

    private suspend fun cargarEquiposSuspend() {
        try {
            _equipos.value = db.collection("equipos").get().await().toObjects(Equipo::class.java)
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error cargando equipos", e)
            _equipos.value = emptyList()
        }
    }

    private suspend fun cargarObrasSuspend() {
        try {
            _obras.value = db.collection("obras").get().await().toObjects(Obra::class.java)
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error cargando obras", e)
            _obras.value = emptyList()
        }
    }

    suspend fun buscarEquipos(codigo: String, tipo: String) {
        val codigoLimpio = codigo.trim()
        val tipoLimpio = tipo.trim()

        if (codigoLimpio.isEmpty() && tipoLimpio.isEmpty()) {
            cargarEquiposSuspend()
        } else {
            try {
                _equipos.value = repository.obtenerEquiposFiltrados(codigoLimpio, tipoLimpio)
            } catch (e: Exception) {
                android.util.Log.e("Firebase", "Error buscando equipos", e)
                _equipos.value = emptyList()
            }
        }
    }

    suspend fun buscarObras(nombreObra: String) {
        val nombreLimpio = nombreObra.trim()
        if (nombreLimpio.isEmpty()) {
            cargarObrasSuspend()
        } else {
            try {
                _obras.value = repository.obtenerObrasFiltradas(nombreLimpio)
            } catch (e: Exception) {
                android.util.Log.e("Firebase", "Error buscando obras", e)
                _obras.value = emptyList()
            }
        }
    }


    suspend fun generarInformePDFequipos(equipos: List<Equipo>): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60
        val paint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
        }

        canvas.drawText("Informe de Equipos", 200f, 40f, paint)

        paint.isFakeBoldText = false
        equipos.forEachIndexed { index, equipo ->
            val text = "${index + 1}. ${equipo.serial} | ${equipo.referencia} | ${equipo.tipo} | ${equipo.fechaCertificacion}"
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 25
        }

        pdfDocument.finishPage(page)

        return try {
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, "Informe_Equipos_${System.currentTimeMillis()}.pdf")

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            "Error al generar PDF: ${e.message}"
        }
    }

    suspend fun generarInformePDFobras(obras: List<Obra>): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60
        val paint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
        }

        canvas.drawText("Informe de Obras", 200f, 40f, paint)

        paint.isFakeBoldText = false
        obras.forEachIndexed { index, obra ->
            val text = "${index + 1}. ${obra.idObra} | ${obra.nombreObra} | ${obra.ubicacion} | ${obra.clienteNombre}"
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 25
        }

        pdfDocument.finishPage(page)

        return try {
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, "Informe_Obras_${System.currentTimeMillis()}.pdf")

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            "Error al generar PDF: ${e.message}"
        }
    }


    @Composable
    fun TablaEquiposFirebase(equipos: List<Equipo>) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Serial", "Referencia", "Tipo", "Fecha").forEach {
                    Text(it, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
            }
            equipos.forEach { eq ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(eq.serial, modifier = Modifier.weight(1f))
                    Text(eq.referencia, modifier = Modifier.weight(1f))
                    Text(eq.tipo, modifier = Modifier.weight(1f))
                    Text(eq.fechaCertificacion, modifier = Modifier.weight(1f))
                }
            }
        }
    }
    @Composable
    fun TablaObrasFirebase(obras: List<Obra>) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("ID Obra", "Nombre Obra", "Ubicacion", "Nombre Cliente").forEach {
                    Text(it, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
            }
            obras.forEach { eq ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(eq.idObra, modifier = Modifier.weight(1f))
                    Text(eq.nombreObra, modifier = Modifier.weight(1f))
                    Text(eq.ubicacion, modifier = Modifier.weight(1f))
                    Text(eq.clienteNombre, modifier = Modifier.weight(1f))
                }
            }
        }
    }
    @Composable
    fun Tabla(titulos: List<String>, filas: List<List<String>>) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                titulos.forEach {
                    Text(it, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
            }
            filas.forEach { fila ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    fila.forEach { valor ->
                        Text(valor, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

}
