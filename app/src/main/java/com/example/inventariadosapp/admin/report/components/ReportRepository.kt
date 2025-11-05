package com.example.inventariadosapp.admin.report.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ReportRepository {

    /**
     * Genera un informe PDF con datos de la colección "asignaciones",
     * lo guarda localmente y lo sube a Firebase Storage.
     */
    fun generarPDFAsignaciones(
        context: Context,
        asignaciones: List<Map<String, Any>>,
        nombreInforme: String,
        onUploadComplete: (String?) -> Unit // devuelve la URL de descarga
    ) {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy_HH-mm", Locale.getDefault())
            val fechaActual = sdf.format(Date())

            // Carpeta de descargas del dispositivo
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, "${nombreInforme}_$fechaActual.pdf")

            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Encabezado del informe
            val titulo = Paragraph("Informe de Asignaciones\n\n")
            titulo.alignment = Element.ALIGN_CENTER
            document.add(titulo)

            val fechaParrafo = Paragraph("Fecha de generación: $fechaActual\n\n")
            fechaParrafo.alignment = Element.ALIGN_RIGHT
            document.add(fechaParrafo)

            // Tabla con la información de las asignaciones
            val table = PdfPTable(7)
            table.widthPercentage = 100f

            val headers = listOf("Serial", "Tipo", "Referencia", "Obra", "Estado Previo", "Usuario", "Fecha Asignación")
            headers.forEach {
                val cell = PdfPCell(Paragraph(it))
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.backgroundColor = com.itextpdf.text.BaseColor.LIGHT_GRAY
                table.addCell(cell)
            }

            for (a in asignaciones) {
                table.addCell((a["serial"] ?: "-").toString())
                table.addCell((a["tipo"] ?: "-").toString())
                table.addCell((a["referencia"] ?: "-").toString())
                table.addCell((a["obraAsignada"] ?: "-").toString())
                table.addCell((a["estadoPrevio"] ?: "-").toString())
                table.addCell((a["usuarioNombre"] ?: "-").toString())

                val fecha = (a["fechaAsignacion"] as? com.google.firebase.Timestamp)?.toDate()
                val fechaStr = fecha?.let {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
                } ?: "-"
                table.addCell(fechaStr)
            }

            document.add(table)
            document.close()

            // 📤 Subir el PDF a Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
            val pdfRef = storageRef.child("informes_asignaciones/${file.name}")

            val uploadTask = pdfRef.putFile(android.net.Uri.fromFile(file))
            uploadTask.addOnSuccessListener {
                pdfRef.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()

                    // Guardar metadatos en Firestore
                    val firestore = FirebaseFirestore.getInstance()
                    val docData = mapOf(
                        "nombreInforme" to file.name,
                        "fechaGeneracion" to Timestamp.now(),
                        "urlDescarga" to url,
                        "totalRegistros" to asignaciones.size
                    )

                    firestore.collection("informes_asignaciones")
                        .add(docData)
                        .addOnSuccessListener {
                            Log.d("ReportRepository", "Informe guardado correctamente en Firestore.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ReportRepository", "Error al guardar en Firestore", e)
                        }

                    onUploadComplete(url)
                }
            }.addOnFailureListener { e ->
                Log.e("ReportRepository", "Error al subir PDF a Storage", e)
                onUploadComplete(null)
            }

        } catch (e: Exception) {
            Log.e("ReportRepository", "Error generando el informe PDF", e)
            onUploadComplete(null)
        }
    }
}