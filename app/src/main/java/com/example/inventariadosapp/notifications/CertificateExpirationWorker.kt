package com.example.inventariadosapp.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.math.ceil

class CertificateExpirationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val db = FirebaseFirestore.getInstance()
        val context = applicationContext

        db.collection("equipos").get().addOnSuccessListener { result ->
            val hoy = Date()

            for (document in result.documents) {
                val nombre = document.getString("nombre") ?: "Equipo"
                val fechaCertStr = document.getString("fecha_certificacion") ?: continue
                val estado = document.getString("estado") ?: "Desconocido"

                try {
                    val fechaCert = Date(fechaCertStr.toLong())
                    val diasRestantes =
                        ceil((fechaCert.time - hoy.time) / (1000 * 60 * 60 * 24.0)).toInt()

                    if (diasRestantes in 1..15 && estado != "Vencido") {
                        NotificationUtils.showNotification(
                            context,
                            "Certificado por vencer",
                            "El certificado de $nombre vence en $diasRestantes días.",
                            nombre.hashCode()
                        )
                    }
                } catch (_: Exception) {
                }
            }
        }

        return Result.success()
    }
}
