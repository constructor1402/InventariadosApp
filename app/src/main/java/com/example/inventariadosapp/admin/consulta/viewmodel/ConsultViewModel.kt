
package com.example.inventariadosapp.admin.consulta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariadosapp.admin.consulta.models.MetricData
import com.example.inventariadosapp.admin.consulta.models.ReportItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConsultViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "ConsultViewModel"

    private val _allReports = MutableStateFlow<List<ReportItem>>(emptyList())
    val allReports: StateFlow<List<ReportItem>> = _allReports.asStateFlow()

    private val _metricas = MutableStateFlow(MetricData())
    val metricas: StateFlow<MetricData> = _metricas.asStateFlow()

    init {
        loadMetrics()
        loadAllReports()
    }

    private fun loadMetrics() {
        firestore.collection("equipos")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                // Contar equipos por estado
                val disponibles = snapshot.documents.count {
                    (it.getString("estado") ?: "") == "Disponible"
                }

                val enUso = snapshot.documents.count {
                    (it.getString("estado") ?: "") == "Asignado"
                }

                // Actualizar métricas en tiempo real
                _metricas.value = MetricData(
                    equiposDisponibles = disponibles,
                    equiposEnUso = enUso
                )
            }
    }



    private fun loadAllReports() {
        viewModelScope.launch {
            try {
                val combinedReports = mutableListOf<ReportItem>()

                // --- 1. Cargar "informes_generados" ---
                val snapshot1 = firestore.collection("informes_generados").get().await()

                val reportsFromCollection1 = snapshot1.documents.mapNotNull { doc ->

                    val item = doc.toObject(ReportItem::class.java)?.copy(
                        id = doc.id,
                        fuenteColeccion = "informes_generados"
                    )

                    item?.copy(fechaReporte = item.fechaGeneracion)
                }
                combinedReports.addAll(reportsFromCollection1)


                // --- 2. Cargar "informes" (Mapeo manual robusto) ---
                val snapshot2 = firestore.collection("informes").get().await()

                val reportsFromCollection2 = snapshot2.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null


                    val serialFromData = data["serial"] as? String

                    val item = ReportItem(
                        id = doc.id,

                        codigo = (data["codigo"] as? String) ?: serialFromData,
                        serial = serialFromData,


                        descripcion = (data["descripcion"] as? String) ?: "",
                        tipo = (data["tipo"] as? String) ?: "",
                        referencia = (data["referencia"] as? String) ?: "",
                        fechaCertificacion = (data["fechaCertificacion"] as? String) ?: "",

                        fechaCreacion = data["fechaCreacion"] as? Timestamp,
                        fuenteColeccion = "informes"
                    )
                    // Establecer la fecha unificada
                    item.copy(fechaReporte = item.fechaCreacion)
                }
                combinedReports.addAll(reportsFromCollection2)

                // --- 3. Combinar, Filtrar y Ordenar ---

                _allReports.value = combinedReports
                    .filter {

                        it.fechaReporte != null || !it.codigo.isNullOrBlank() || !it.serial.isNullOrBlank()
                    }
                    .sortedByDescending { it.fechaReporte }

            } catch (e: Exception) {

                Log.e(TAG, "Error fatal al cargar todos los informes: ${e.message}", e)
                _allReports.value = emptyList()

            }
        }
    }
}