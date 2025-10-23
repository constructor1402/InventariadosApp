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
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserRepository
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class InformeEquiposViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val repository = UserRepository()

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()

    init {
        cargarEquipos()
    }

    fun cargarEquipos() {
        db.collection("equipos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    doc.toObject(Equipo::class.java)
                }
                _equipos.value = lista
            }
    }
    fun buscarEquipos(codigo: String, estado: String) {
        viewModelScope.launch {
            if (codigo.isBlank() && estado.isBlank()) {
                // Mostrar todos los equipos
                cargarEquipos()
            } else {
                _equipos.value = repository.obtenerEquiposFiltrados(codigo, estado)
            }
        }
    }

    suspend fun generarInformePDF(equipos: List<Equipo>): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60
        val paint = android.graphics.Paint().apply {
            textSize = 14f
        }

        canvas.drawText("Informe de Equipos", 200f, 40f, paint)

        equipos.forEachIndexed { index, equipo ->
            val text = "${index + 1}. ${equipo.serial} | ${equipo.referencia} | ${equipo.tipo} | ${equipo.fechaCertificacion}"
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 25
        }

        pdfDocument.finishPage(page)

        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Informe_Equipos.pdf"
        )
        pdfDocument.writeTo(FileOutputStream(path))
        pdfDocument.close()

        return path.absolutePath
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
}


