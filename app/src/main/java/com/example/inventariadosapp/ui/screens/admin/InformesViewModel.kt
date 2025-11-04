package com.example.inventariadosapp.ui.screens.admin


import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.screens.admin.gestion.Obra
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserRepository
import com.example.inventariadosapp.ui.admin.users_DiegoFaj.UserUiState
import com.example.inventariadosapp.ui.screens.admin.gestion.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.graphics.Color
class InformeEquiposViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val repository = UserRepository()
    private val _informesUsuario = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val informesUsuario: StateFlow<List<Map<String, Any>>> = _informesUsuario.asStateFlow()

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    private val _obras = MutableStateFlow<List<Obra>>(emptyList())

    private val _users = MutableStateFlow<List<UserUiState>>(emptyList())


    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()
    val obras: StateFlow<List<Obra>> = _obras.asStateFlow()
    val users: StateFlow<List<UserUiState>> = _users.asStateFlow()

    init {
        viewModelScope.launch {
            cargarEquiposSuspend()
            cargarObrasSuspend()
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

        try {
            _obras.value = if (nombreLimpio.isEmpty()) {
                // Si no se digitó nada, traer TODAS las obras
                db.collection("obras").get().await().toObjects(Obra::class.java)
            } else {
                // Si se digitó algo, traer solo las que coincidan con el nombre
                repository.obtenerObrasFiltradas(nombreLimpio)
            }
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error buscando obras", e)
            _obras.value = emptyList()
        }
    }


    suspend fun buscarUsuarios(nombreUser: String, correoUser: String) {
        val nombreLimpio = nombreUser.trim()
        val correoLimpio = correoUser.trim()

        try {
            val usuariosRef = db.collection("usuarios")
            var query = usuariosRef.limit(50) // límite preventivo

            when {
                nombreLimpio.isNotEmpty() && correoLimpio.isNotEmpty() -> {
                    // Filtra por ambos campos
                    query = usuariosRef
                        .whereEqualTo("nombre", nombreLimpio)
                        .whereEqualTo("correo", correoLimpio)
                }

                nombreLimpio.isNotEmpty() -> {
                    // Filtra por nombre
                    query = usuariosRef.whereGreaterThanOrEqualTo("nombre", nombreLimpio)
                        .whereLessThanOrEqualTo("nombre", nombreLimpio + '\uf8ff')
                }

                correoLimpio.isNotEmpty() -> {
                    // Filtra por correo
                    query = usuariosRef.whereEqualTo("correo", correoLimpio)
                }

                else -> {
                    // Si no hay filtros, carga todos
                    _users.value = db.collection("usuarios").get().await().toObjects(UserUiState::class.java)
                    return
                }
            }

            val snapshot = query.get().await()
            _users.value = snapshot.toObjects(UserUiState::class.java)

        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error buscando usuarios", e)
            _users.value = emptyList()
        }
    }

    suspend fun generarInformePDFequipos(
        equipos: List<Equipo>,
        usuarioCorreo: String
    ): String {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        var pageNumber = 1
        var pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = android.graphics.Paint().apply {
            textSize = 18f
            isFakeBoldText = true
        }

        val headerPaint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.rgb(102, 134, 232) // Azul similar al de la app
        }

        val cellPaint = android.graphics.Paint().apply {
            textSize = 12f
        }

        // 🔹 Márgenes y separaciones
        val startX = 40f
        var y = 60

        // 🔹 Título y usuario
        canvas.drawText("Informe de Equipos", 200f, 40f, titlePaint)
        canvas.drawText("Generado por:", startX, y.toFloat(), cellPaint)
        y += 20
        canvas.drawText(" $usuarioCorreo", startX, y.toFloat(), cellPaint)
        y += 30 // Separación adicional

        // 🔹 Encabezado de tabla
        val headers = listOf("Serial", "Referencia", "Tipo", "Fecha")
        val colWidth = 130f
        var x = startX

        headers.forEach {
            canvas.drawText(it, x, y.toFloat(), headerPaint)
            x += colWidth
        }

        y += 25

        // 🔹 Filas de datos
        equipos.forEachIndexed { index, equipo ->
            // Si llega al final de la página → nueva
            if (y > 800) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 60

                canvas.drawText("Informe de Equipos (cont.)", 200f, 40f, titlePaint)
                y += 30
                x = startX
                headers.forEach {
                    canvas.drawText(it, x, y.toFloat(), headerPaint)
                    x += colWidth
                }
                y += 25
            }

            // Alternar color de fondo en filas
            val bgPaint = android.graphics.Paint().apply {
                color = if (index % 2 == 0) android.graphics.Color.rgb(247, 248, 252)
                else android.graphics.Color.WHITE
                style = android.graphics.Paint.Style.FILL
            }

            // Dibujar fondo de fila
            canvas.drawRect(startX - 10, (y - 15).toFloat(), 560f, (y + 10).toFloat(), bgPaint)

            // Dibujar datos
            x = startX
            listOf(
                equipo.serial,
                equipo.referencia,
                equipo.tipo,
                equipo.fechaCertificacion
            ).forEach {
                canvas.drawText(it, x, y.toFloat(), cellPaint)
                x += colWidth
            }
            y += 22
        }

        pdfDocument.finishPage(page)

        return try {
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(downloads, "Informe_Equipos_${System.currentTimeMillis()}.pdf")
            pdfDocument.writeTo(java.io.FileOutputStream(file))
            pdfDocument.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            "Error al generar PDF: ${e.message}"
        }
    }



    suspend fun generarInformePDFobras(
        obras: List<Obra>,
        userCorreo: String
    ): String {
        val pdfDocument = PdfDocument()
        var pageNumber = 1

        // 📄 Tamaño A4 horizontal (842x595)
        var pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // 🎨 Estilos
        val titlePaint = android.graphics.Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val subtitlePaint = android.graphics.Paint().apply {
            textSize = 12f
            textAlign = android.graphics.Paint.Align.CENTER
            color = android.graphics.Color.DKGRAY
        }

        val headerPaint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.WHITE
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val cellPaint = android.graphics.Paint().apply {
            textSize = 12f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val idPaint = android.graphics.Paint().apply {
            textSize = 11f
            textAlign = android.graphics.Paint.Align.CENTER
            color = android.graphics.Color.rgb(120, 120, 120)
        }

        val bgHeaderPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(102, 134, 232)
        }

        val bgRowEven = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(239, 241, 249)
        }

        val bgRowOdd = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
        }

        val linePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }

        // 📐 Márgenes y ancho total
        val left = 40f
        val right = 802f
        var y = 80f

        // 🧭 Título centrado con más espacio entre título y correo
        canvas.drawText("Informe de Obras", 421f, 60f, titlePaint)
        canvas.drawText("Generado para el usuario: $userCorreo", 421f, 85f, subtitlePaint)
        y = 115f // 🔹 empezamos la tabla un poco más abajo

        // 🧱 Encabezado
        val columnWidths = listOf(120f, 250f, 200f, 170f) // más espacio horizontal
        val headers = listOf("ID Obra", "Nombre Obra", "Ubicación", "Cliente")
        val rowHeight = 28f

        var x = left
        canvas.drawRect(left, y - 18, right, y + rowHeight, bgHeaderPaint)
        headers.forEachIndexed { i, header ->
            val cellCenter = x + columnWidths[i] / 2
            canvas.drawText(header, cellCenter, y + 3, headerPaint)
            x += columnWidths[i]
        }
        y += rowHeight + 10

        // 📋 Filas
        obras.forEachIndexed { index, obra ->
            val bgPaint = if (index % 2 == 0) bgRowEven else bgRowOdd

            // Nueva página si se llena el espacio
            if (y + rowHeight > 560f) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 80f
                canvas.drawText("Informe de Obras (cont.)", 421f, 50f, titlePaint)
            }

            // Fondo fila
            canvas.drawRect(left, y - 15, right, y + rowHeight, bgPaint)

            // Contenido
            val idCorto = if (obra.idObra.length > 10) obra.idObra.take(10) + "..." else obra.idObra
            val values = listOf(
                idCorto,
                obra.nombreObra,
                obra.ubicacion,
                obra.clienteNombre
            )

            x = left
            values.forEachIndexed { i, text ->
                val cellCenter = x + columnWidths[i] / 2
                val paintToUse = if (i == 0) idPaint else cellPaint
                canvas.drawText(text, cellCenter, y + 3, paintToUse)
                x += columnWidths[i]
            }

            // Línea separadora
            canvas.drawLine(left, y + rowHeight, right, y + rowHeight, linePaint)
            y += rowHeight + 10
        }

        pdfDocument.finishPage(page)

        // 💾 Guardar
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






    suspend fun guardarInformeEnFirebase(filePath: String, tipo: String, userCorreo: String) {
        val storage = com.google.firebase.storage.FirebaseStorage.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        try {
            val file = java.io.File(filePath)
            val fileUri = android.net.Uri.fromFile(file)
            val storageRef = storage.reference.child("informes/$userCorreo/${file.name}")

            // Subir PDF al Storage
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Guardar registro en Firestore
            val informeData = hashMapOf(
                "correoUsuario" to userCorreo,
                "tipo" to tipo,
                "fecha" to com.google.firebase.Timestamp.now(),
                "nombreArchivo" to file.name,
                "url" to downloadUrl
            )

            db.collection("informes").add(informeData).await()

            android.util.Log.d("Firebase", "Informe guardado correctamente")
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error guardando informe", e)
        }
    }

    @Composable
    fun TablaInformesUsuario(informes: List<Map<String, Any>>) {
        val context = LocalContext.current
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Tipo", "Fecha", "Archivo").forEach {
                    Text(it, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
            }
            informes.forEach { inf ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(inf["tipo"].toString(), modifier = Modifier.weight(1f))
                    Text(inf["fecha"].toString(), modifier = Modifier.weight(1f))
                    Text(
                        text = "Abrir",
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                val url = inf["urlPDF"].toString()
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                        color =Color(0xFF1E3A8A)
                    )
                }
            }
        }
    }



}
