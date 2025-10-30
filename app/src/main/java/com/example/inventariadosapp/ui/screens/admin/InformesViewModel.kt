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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
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
    suspend fun cargarInformesUsuario(correoUsuario: String) {
        try {
            val snapshot = db.collection("informes")
                .whereEqualTo("usuarioCorreo", correoUsuario)
                .get()
                .await()

            _informesUsuario.value = snapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "Error cargando informes del usuario", e)
            _informesUsuario.value = emptyList()
        }
    }
    fun obtenerUsuarioActual(): Pair<String, String>? {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val user = auth.currentUser
        return if (user != null) {
            val uid = user.uid
            val correo = user.email ?: "Sin correo"
            Pair(uid, correo)
        } else {
            null
        }
    }

    suspend fun generarInformePDFequipos(
        equipos: List<Equipo>,
        usuarioNombre: String,
        usuarioCorreo: String
    ): String {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
        }

        var y = 60
        canvas.drawText("Informe de Equipos", 200f, 40f, paint)

        paint.isFakeBoldText = false
        canvas.drawText("Generado por: $usuarioNombre ($usuarioCorreo)", 40f, (y + 20).toFloat(), paint)
        y += 60

        equipos.forEachIndexed { index, equipo ->
            val text = "${index + 1}. ${equipo.serial} | ${equipo.referencia} | ${equipo.tipo} | ${equipo.fechaCertificacion}"
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 25
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
        usuarioNombre: String,
        usuarioCorreo: String
    ): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = android.graphics.Paint().apply {
            textSize = 14f
            isFakeBoldText = true
        }

        var y = 60
        canvas.drawText("Informe de Obras", 200f, 40f, paint)

        paint.isFakeBoldText = false
        canvas.drawText("Generado por: $usuarioNombre ($usuarioCorreo)", 40f, (y + 20).toFloat(), paint)
        y += 60

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

    suspend fun guardarInformeEnFirebase(usuarioId: String, filePath: String, tipo: String) {
        val storage = com.google.firebase.storage.FirebaseStorage.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        try {
            val file = java.io.File(filePath)
            val fileUri = android.net.Uri.fromFile(file)
            val storageRef = storage.reference.child("informes/${file.name}")

            // Subir PDF al Storage
            val uploadTask = storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Guardar registro en Firestore
            val informeData = hashMapOf(
                "usuarioId" to usuarioId,
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
