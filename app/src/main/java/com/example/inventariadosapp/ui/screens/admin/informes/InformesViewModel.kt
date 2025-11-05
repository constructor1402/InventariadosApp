package com.example.inventariadosapp.ui.screens.admin.informes


import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.screens.admin.gestion.Obra
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
import androidx.compose.ui.unit.dp
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserRepository
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserUiState
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage

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
            Log.e("Firebase", "Error cargando equipos", e)
            _equipos.value = emptyList()
        }
    }

    private suspend fun cargarObrasSuspend() {
        try {
            _obras.value = db.collection("obras").get().await().toObjects(Obra::class.java)
        } catch (e: Exception) {
            Log.e("Firebase", "Error cargando obras", e)
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
                Log.e("Firebase", "Error buscando equipos", e)
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
            Log.e("Firebase", "Error buscando obras", e)
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
            Log.e("Firebase", "Error buscando usuarios", e)
            _users.value = emptyList()
        }
    }

    suspend fun generarInformePDFequipos(
        equipos: List<Equipo>,
        usuarioCorreo: String
    ): String {
        val pdfDocument = PdfDocument()
        var pageNumber = 1

        // 📄 Tamaño A4 horizontal
        var pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // 🎨 Estilos
        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = Paint().apply {
            textSize = 12f
            textAlign = Paint.Align.CENTER
            color = android.graphics.Color.DKGRAY
        }

        val headerPaint = Paint().apply {
            textSize = 13f
            isFakeBoldText = true
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
        }

        val cellPaint = Paint().apply {
            textSize = 11f
            textAlign = Paint.Align.CENTER
            color = android.graphics.Color.BLACK
        }

        val bgHeaderPaint = Paint().apply {
            color = android.graphics.Color.rgb(102, 134, 232)
        }

        val bgRowEven = Paint().apply {
            color = android.graphics.Color.rgb(247, 248, 252)
        }

        val bgRowOdd = Paint().apply {
            color = android.graphics.Color.WHITE
        }

        val linePaint = Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }

        // 📐 Márgenes y disposición general
        val left = 30f
        val right = 812f
        var y = 100f

        // 🧭 Título centrado
        canvas.drawText("Informe de Equipos", 421f, 50f, titlePaint)
        canvas.drawText("Generado por: $usuarioCorreo", 421f, 70f, subtitlePaint)

        // 🧱 Encabezado de tabla
        val headers = listOf("Serial", "Referencia", "Tipo", "Estado", "Obra", "Fecha", "Descripción")
        val colWidths = listOf(80f, 100f, 80f, 80f, 120f, 100f, 180f)
        val rowHeight = 26f

        var x = left
        canvas.drawRect(left, y - 18, right, y + rowHeight, bgHeaderPaint)
        headers.forEachIndexed { i, header ->
            val cellCenter = x + colWidths[i] / 2
            canvas.drawText(header, cellCenter, y + 3, headerPaint)
            x += colWidths[i]
        }
        y += rowHeight + 10

        // 📋 Filas de datos
        equipos.forEachIndexed { index, equipo ->
            val bgPaint = if (index % 2 == 0) bgRowEven else bgRowOdd

            // Salto de página si se llena
            if (y + rowHeight > 550f) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 80f
                canvas.drawText("Informe de Equipos (cont.)", 421f, 50f, titlePaint)
            }

            // Fondo de fila
            canvas.drawRect(left, y - 15, right, y + rowHeight, bgPaint)

            // Datos
            val descripcionCorta = equipo.descripcion.take(40) + if (equipo.descripcion.length > 40) "..." else ""
            val values = listOf(
                equipo.serial,
                equipo.referencia,
                equipo.tipo,
                equipo.estado,
                equipo.obra,
                equipo.fechaCertificacion,
                descripcionCorta
            )

            x = left
            values.forEachIndexed { i, value ->
                val cellCenter = x + colWidths[i] / 2
                canvas.drawText(value, cellCenter, y + 3, cellPaint)
                x += colWidths[i]
            }

            // Línea separadora
            canvas.drawLine(left, y + rowHeight, right, y + rowHeight, linePaint)
            y += rowHeight + 10
        }

        pdfDocument.finishPage(page)

        // 💾 Guardar archivo
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
        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = Paint().apply {
            textSize = 12f
            textAlign = Paint.Align.CENTER
            color = android.graphics.Color.DKGRAY
        }

        val headerPaint = Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
        }

        val cellPaint = Paint().apply {
            textSize = 12f
            textAlign = Paint.Align.CENTER
        }

        val idPaint = Paint().apply {
            textSize = 11f
            textAlign = Paint.Align.CENTER
            color = android.graphics.Color.rgb(120, 120, 120)
        }

        val bgHeaderPaint = Paint().apply {
            color = android.graphics.Color.rgb(102, 134, 232)
        }

        val bgRowEven = Paint().apply {
            color = android.graphics.Color.rgb(239, 241, 249)
        }

        val bgRowOdd = Paint().apply {
            color = android.graphics.Color.WHITE
        }

        val linePaint = Paint().apply {
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
        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()

        try {
            val file = File(filePath)
            val fileUri = Uri.fromFile(file)
            val storageRef = storage.reference.child("informes/$userCorreo/${file.name}")

            // Subir PDF al Storage
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Guardar registro en Firestore
            val informeData = hashMapOf(
                "correoUsuario" to userCorreo,
                "tipo" to tipo,
                "fecha" to Timestamp.now(),
                "nombreArchivo" to file.name,
                "url" to downloadUrl
            )

            db.collection("informes").add(informeData).await()

            Log.d("Firebase", "Informe guardado correctamente")
        } catch (e: Exception) {
            Log.e("Firebase", "Error guardando informe", e)
        }
    }
    suspend fun cargarInformesUsuario(correo: String) {
        try {
            val snapshot = db.collection("informes")
                .whereEqualTo("correoUsuario", correo)
                .get()
                .await()

            _informesUsuario.value = snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            Log.e("Firebase", "Error cargando informes del usuario", e)
            _informesUsuario.value = emptyList()
        }
    }

    // Generar informe general (tabla de informes)
    suspend fun generarInformePDFinformes(
        informes: List<Map<String, Any>>,
        userCorreo: String
    ): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
        }

        val headerPaint = Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.rgb(102, 134, 232)
        }

        val textPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.BLACK
        }

        val startX = 40f
        var y = 60f

        canvas.drawText("Informe de Actividad de Usuario", 150f, 40f, titlePaint)
        canvas.drawText("Usuario: $userCorreo", startX, y, textPaint)
        y += 30f

        val headers = listOf("Nombre Archivo", "Tipo", "Fecha")
        val colWidth = 170f
        var x = startX
        headers.forEach {
            canvas.drawText(it, x, y, headerPaint)
            x += colWidth
        }
        y += 25f

        informes.forEach {
            val nombre = it["nombreArchivo"]?.toString() ?: "-"
            val tipo = it["tipo"]?.toString() ?: "-"
            val fecha = it["fecha"]?.toString()?.substringBefore("Timestamp") ?: "-"
            x = startX
            listOf(nombre, tipo, fecha).forEach { text ->
                canvas.drawText(text, x, y, textPaint)
                x += colWidth
            }
            y += 20f
        }

        pdfDocument.finishPage(page)

        return try {
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, "Informe_Usuario_${System.currentTimeMillis()}.pdf")
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file.absolutePath
        } catch (e: Exception) {
            pdfDocument.close()
            "Error al generar PDF: ${e.message}"
        }
    }


    @Composable
    fun TablaInformesUsuario(informes: List<Map<String, Any>>) {
        val context = LocalContext.current

        Column(Modifier.fillMaxWidth()) {
            if (informes.isEmpty()) {
                Text(
                    text = "No hay informes generados todavía.",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    fontWeight = FontWeight.Medium
                )
                return
            }

            // Encabezado
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Tipo", "Fecha", "Archivo").forEach {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Filas
            informes.forEach { inf ->
                val tipo = inf["tipo"]?.toString() ?: "-"
                val url = inf["url"]?.toString() ?: ""
                val timestamp = inf["fecha"] as? com.google.firebase.Timestamp
                val fechaFormateada = timestamp?.toDate()?.let {
                    java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(it)
                } ?: "-"

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(tipo, modifier = Modifier.weight(1f))
                    Text(fechaFormateada, modifier = Modifier.weight(1f))
                    Text(
                        text = if (url.isNotEmpty()) "Abrir" else "-",
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = url.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                        color = if (url.isNotEmpty()) Color(0xFF1E3A8A) else Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }




}
