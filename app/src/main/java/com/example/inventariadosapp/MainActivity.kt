package com.example.inventariadosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventariadosapp.navigation.AppNavigation
import com.example.inventariadosapp.ui.theme.InventariadosAppTheme
import com.example.inventariadosapp.notifications.NotificationUtils
import com.google.firebase.firestore.FirebaseFirestore
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.inventariadosapp.notifications.CertificateExpirationWorker
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 🔹 Programa la verificación diaria de certificados
        val workRequest = PeriodicWorkRequestBuilder<CertificateExpirationWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)


        // 🔹 Crea el canal de notificaciones locales (solo una vez)
        NotificationUtils.createNotificationChannel(this)

        setContent {
            InventariadosAppTheme {
                AppNavigation()
            }
        }

        // 🔹 Verifica si hay equipos nuevos o eliminados al ingresar
        verificarCambiosDeEquipos()
    }

    private fun verificarCambiosDeEquipos() {
        val db = FirebaseFirestore.getInstance()
        db.collection("equipos").get().addOnSuccessListener { result ->
            val equiposActuales = result.documents.map { it.id }

            val prefs = getSharedPreferences("inventariados_prefs", MODE_PRIVATE)
            val equiposPrevios = prefs.getStringSet("equipos_previos", emptySet()) ?: emptySet()

            val nuevos = equiposActuales.minus(equiposPrevios)
            val eliminados = equiposPrevios.minus(equiposActuales)

            if (nuevos.isNotEmpty()) {
                NotificationUtils.showNotification(
                    this,
                    "Nuevos equipos añadidos",
                    "Se agregaron: ${nuevos.joinToString(", ")}",
                    1001
                )
            }

            if (eliminados.isNotEmpty()) {
                NotificationUtils.showNotification(
                    this,
                    "Equipos eliminados",
                    "Se eliminaron: ${eliminados.joinToString(", ")}",
                    1002
                )
            }

            // 🔹 Guarda el nuevo estado de la lista
            prefs.edit().putStringSet("equipos_previos", equiposActuales.toSet()).apply()
        }
    }
}
