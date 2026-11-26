package com.example.programacion_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> SelectorFormulario(
    titulo: String,
    opciones: List<T>,
    seleccionado: T?,
    etiqueta: (T) -> String,
    onSeleccionar: (T) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    habilitado: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth().semantics {
            error?.let { mensaje -> error(mensaje) }
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleSmall,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            opciones.forEach { opcion ->
                FilterChip(
                    selected = seleccionado == opcion,
                    onClick = { onSeleccionar(opcion) },
                    label = { Text(etiqueta(opcion)) },
                    enabled = habilitado,
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
