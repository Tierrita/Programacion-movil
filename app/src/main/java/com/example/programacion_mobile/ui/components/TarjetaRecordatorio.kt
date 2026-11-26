package com.example.programacion_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio

@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Alta -> colores.error
        Prioridad.Media -> colores.tertiary
        Prioridad.Baja -> colores.primary
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = "Categoría: " +
                    RecordatoriosSimulados.etiquetas.getValue(
                        recordatorio.categoria
                    ),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "Prioridad: ${recordatorio.prioridad.etiqueta}",
                color = colorPrioridad,
            )

            Text(
                text = "Estado: ${recordatorio.estado.etiqueta}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
