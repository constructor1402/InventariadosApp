package com.example.inventariadosapp.admin.report.components.models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.core.content.FileProvider
import com.example.inventariadosapp.admin.report.reportnavgraph.ReportRoutes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun ReportSuccessScreen(
    navController: NavController,
    codigoBuscado: String
) {
    val context = LocalContext.current
    var generando by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✅ ¡Informe generado con éxito!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Generar PDF & subir informe
                generando = true
                generarPdfYGuardar(context, codigoBuscado) { ok, msg ->
                    generando = false
                    mensaje = msg
                    if (ok) {
                        Toast.makeText(context, "PDF guardado y subido correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            if (generando) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Descargar PDF", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🟢 BOTÓN SALIR
        OutlinedButton(
            onClick = { navController.popBackStack(route = ReportRoutes.SEARCH, inclusive = false) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50))
        ) {
            Text("Salir")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = mensaje)
        }
    }
}

fun generarPdfYGuardar(context: Context, codigoBuscado: String, onFinish: (ok: Boolean, msg: String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val query = codigoBuscado.trim().uppercase()

    // Primero buscar por campo serial
    db.collection("equipos").whereEqualTo("serial", query).get()
        .addOnSuccessListener { snap ->
            if (!snap.isEmpty) {
                val doc = snap.documents.first()
                val data = doc.data ?: emptyMap<String, Any>()
                generarPdfConEquipo(context, query, data, db, onFinish)
            } else {
                // Buscar por ID del documento
                db.collection("equipos").document(query).get()
                    .addOnSuccessListener { docSnap ->
                        if (docSnap.exists()) {
                            val data = docSnap.data ?: emptyMap<String, Any>()
                            generarPdfConEquipo(context, query, data, db, onFinish)
                        } else {
                            onFinish(false, "No se encontró el equipo.")
                        }
                    }
                    .addOnFailureListener { e ->
                        onFinish(false, "Error buscando equipo: ${e.message}")
                    }
            }
        }
        .addOnFailureListener { e ->
            onFinish(false, "Error buscando equipo: ${e.message}")
        }
}

private fun generarPdfConEquipo(
    context: Context,
    codigo: String,
    equipo: Map<String, Any>,
    db: FirebaseFirestore,
    onFinish: (ok: Boolean, msg: String) -> Unit
) {
    try {

        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: context.filesDir // fallback
        if (!dir.exists()) dir.mkdirs()

        val fileName = "Informe_${codigo}.pdf"
        val file = File(dir, fileName)


        val pdfDoc = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Informe de Equipo: $codigo", 40f, 60f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        var y = 100f

        // Escribir campos relevantes
        val keys = listOf("serial", "descripcion", "tipo", "referencia", "fechaCertificacion")
        for (k in keys) {
            val v = equipo[k]?.toString() ?: ""
            canvas.drawText("${k.replaceFirstChar { it.uppercase() }}: $v", 40f, y, paint)
            y += 22f
        }

        pdfDoc.finishPage(page)

        // escribir a archivo
        FileOutputStream(file).use { out ->
            pdfDoc.writeTo(out)
        }
        pdfDoc.close()


        val informe = hashMapOf(
            "codigo" to (equipo["serial"] ?: codigo),
            "descripcion" to (equipo["descripcion"] ?: ""),
            "tipo" to (equipo["tipo"] ?: ""),
            "referencia" to (equipo["referencia"] ?: ""),
            "fechaCertificacion" to (equipo["fechaCertificacion"] ?: ""),
            "fechaGeneracion" to Timestamp.now()
        )

        db.collection("informes_generados").add(informe)
            .addOnSuccessListener {

                try {
                    val authority = "${context.packageName}.provider"
                    val uri: Uri = FileProvider.getUriForFile(context, authority, file)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(Intent.createChooser(intent, "Abrir informe PDF"))
                    onFinish(true, "PDF generado: $fileName")
                } catch (e: Exception) {
                    onFinish(false, "PDF creado pero no se pudo abrir: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                onFinish(false, "PDF creado pero fallo al subir informe: ${e.message}")
            }

    } catch (e: IOException) {
        onFinish(false, "Error al generar PDF: ${e.message}")
    } catch (e: Exception) {
        onFinish(false, "Error inesperado: ${e.message}")
    }
}