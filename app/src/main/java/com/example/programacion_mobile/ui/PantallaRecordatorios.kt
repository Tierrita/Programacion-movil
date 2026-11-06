package com.example.programacion_mobile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio
import com.example.programacion_mobile.ui.theme.ProgramacionMobileTheme

@Composable
fun PantallaRecordatorios() {
    var soloPendientes by rememberSaveable { mutableStateOf(false) }
    var soloPrioridadAlta by rememberSaveable { mutableStateOf(false) }
    val datos = RecordatoriosSimulados
    val recordatorios = datos.filtrar(
        estado = if (soloPendientes) Estado.Pendiente else null,
        prioridad = if (soloPrioridadAlta) Prioridad.Alta else null,
    )

    Scaffold { espacioDelSistema ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(espacioDelSistema).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            item {
                Text("Recordatorios", style = MaterialTheme.typography.headlineMedium)
                Text("Categorías disponibles: ${datos.categorias.size}")
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = soloPendientes,
                        onClick = { soloPendientes = !soloPendientes },
                        label = { Text("Pendientes") },
                    )
                    FilterChip(
                        selected = soloPrioridadAlta,
                        onClick = { soloPrioridadAlta = !soloPrioridadAlta },
                        label = { Text("Prioridad alta") },
                    )
                }
            }

            if (recordatorios.isEmpty()) {
                item { Text("No hay recordatorios para estos filtros.") }
            }

            items(recordatorios, key = { it.id }) { recordatorio ->
                TarjetaRecordatorio(recordatorio, datos.etiquetas.getValue(recordatorio.categoria))
            }
        }
    }
}

@Composable
private fun TarjetaRecordatorio(recordatorio: Recordatorio, categoria: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(recordatorio.titulo, style = MaterialTheme.typography.titleMedium)
            Text("Categoría: $categoria")
            Text("Prioridad: ${recordatorio.prioridad.etiqueta}")
            Text("Estado: ${recordatorio.estado.etiqueta}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaRecordatorios() {
    ProgramacionMobileTheme {
        PantallaRecordatorios()
    }
}
