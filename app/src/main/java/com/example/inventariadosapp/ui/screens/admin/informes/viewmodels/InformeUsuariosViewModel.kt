package com.example.inventariadosapp.ui.screens.admin.informes.viewmodels

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.inventariadosapp.ui.screens.admin.gestion.users.UserUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

class InformeUsuariosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // 🔹 Cargar todos los usuarios de Firestore
    suspend fun obtenerUsuarios(): List<UserUiState> {
        return try {
            val snapshot = db.collection("usuarios").get().await()
            snapshot.documents.mapNotNull { doc ->
                UserUiState(
                    idUsuario = doc.id,
                    nombre = doc.getString("nombreCompleto") ?: "",
                    celular = doc.getString("numeroCelular") ?: "",
                    correo = doc.getString("correoElectronico") ?: "",
                    contrasena = doc.getString("contrasena") ?: "",
                    rol = doc.getString("rolSeleccionado") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Error cargando usuarios", e)
            emptyList()
        }
    }

    // 🔹 Generar PDF con todos los usuarios
    suspend fun generarInformePDFusuarios(usuarios: List<UserUiState>, userCorreo: String): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // 🎨 Estilos
        val titlePaint = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }

        val headerPaint = Paint().apply {
            textSize = 12f
            isFakeBoldText = true
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
        }

        val cellPaint = Paint().apply {
            textSize = 11f
            textAlign = Paint.Align.CENTER
        }

        val bgHeaderPaint = Paint().apply {
            color = android.graphics.Color.rgb(102, 134, 232)
        }

        // 📐 Layout de tabla
        val left = 30f
        val right = 812f
        var y = 80f
        val rowHeight = 26f
        val colWidths = listOf(150f, 200f, 140f, 120f, 100f)
        val headers = listOf("Nombre", "Correo", "Celular", "Rol", "Contraseña")

        // 🧭 Encabezado
        canvas.drawText("Informe de Usuarios", 421f, 50f, titlePaint)
        canvas.drawRect(left, y - 18, right, y + rowHeight, bgHeaderPaint)

        var x = left
        headers.forEachIndexed { i, header ->
            val center = x + colWidths[i] / 2
            canvas.drawText(header, center, y + 3, headerPaint)
            x += colWidths[i]
        }
        y += rowHeight + 10

        // 📋 Filas de datos
        usuarios.forEach { user ->
            val values = listOf(
                user.nombre,
                user.correo,
                user.celular,
                user.rol,
                user.contrasena
            )

            x = left
            values.forEachIndexed { i, value ->
                val center = x + colWidths[i] / 2
                canvas.drawText(value, center, y, cellPaint)
                x += colWidths[i]
            }
            y += rowHeight
        }

        pdfDocument.finishPage(page)

        // 💾 Guardar archivo
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloads, "Informe_Usuarios_${System.currentTimeMillis()}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file.absolutePath
    }
}

