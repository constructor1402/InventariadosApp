package com.example.inventariadosapp.admin.report.components.models

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 📘 ReportRepository:
 * - Genera informes de equipos consultando Firestore.
 * - Guarda los informes en Firestore (colección "informes").
 * - Genera un PDF local en el dispositivo con los resultados.
 */
class ReportRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // ======================================================
    // 🔹 CONSULTAR EQUIPOS EN FIRESTORE
    // ======================================================
    fun obtenerEquipos(
        codigoOBusqueda: String?,
        estado: String?,
        categoria: String?,
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onError: (String) -> Unit
    ) {
        var query = firestore.collection("equipos").limit(50)

        if (!codigoOBusqueda.isNullOrEmpty()) {
            query = query.whereEqualTo("serial", codigoOBusqueda)
        }
        if (!estado.isNullOrEmpty()) {
            query = query.whereEqualTo("estado", estado)
        }
        if (!categoria.isNullOrEmpty()) {
            query = query.whereEqualTo("tipo", categoria)
        }

        query.get()
            .addOnSuccessListener { snapshot ->
                val equipos = snapshot.documents.mapNotNull { it.data }
                onSuccess(equipos)
            }
            .addOnFailureListener { e ->
                onError("Error al obtener los equipos: ${e.message}")
            }
    }

    // ======================================================
    // 🔹 GUARDAR INFORME EN FIRESTORE
    // ======================================================
    fun guardarInformeEnFirestore(
        equipos: List<Map<String, Any>>,
        nombreInforme: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val fechaActual = Timestamp.now()

        val data = hashMapOf(
            "nombreInforme" to nombreInforme,
            "fechaCreacion" to fechaActual,
            "totalEquipos" to equipos.size,
            "detalles" to equipos
        )

        firestore.collection("informes")
            .add(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("Error al guardar el informe: ${e.message}")
            }
    }

    // ======================================================
    // 🔹 GENERAR PDF LOCAL
    // ======================================================
    fun generarPDF(context: Context, equipos: List<Map<String, Any>>, nombreInforme: String) {
        try {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val pdfFile = File(downloadsDir, "$nombreInforme.pdf")

            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()

            val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            document.add(Paragraph("📋 $nombreInforme\n\n"))
            document.add(Paragraph("Fecha de generación: $date\n\n"))
            document.add(Paragraph("Total de equipos: ${equipos.size}\n\n"))

            equipos.forEachIndexed { index, equipo ->
                document.add(Paragraph("Equipo ${index + 1}:"))
                document.add(Paragraph("• Serial: ${equipo["serial"] ?: "N/A"}"))
                document.add(Paragraph("• Referencia: ${equipo["referencia"] ?: "N/A"}"))
                document.add(Paragraph("• Tipo: ${equipo["tipo"] ?: "N/A"}"))
                document.add(Paragraph("• Descripción: ${equipo["descripcion"] ?: "N/A"}"))
                document.add(Paragraph("• Fecha Certificación: ${equipo["fechaCertificacion"] ?: "N/A"}"))
                document.add(Paragraph("• Estado: ${equipo["estado"] ?: "N/A"}"))
                document.add(Paragraph("\n------------------------------\n"))
            }

            document.close()

            Toast.makeText(
                context,
                "✅ Informe guardado en Descargas como '$nombreInforme.pdf'",
                Toast.LENGTH_LONG
            ).show()

            // Además, guarda registro en Firestore
            guardarInformeEnFirestore(equipos, nombreInforme,
                onSuccess = {
                    Log.d("ReportRepository", "Informe guardado exitosamente en Firestore.")
                },
                onError = {
                    Log.e("ReportRepository", it)
                }
            )

        } catch (e: Exception) {
            Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("ReportRepository", "Error al generar PDF", e)
        }
    }
}
