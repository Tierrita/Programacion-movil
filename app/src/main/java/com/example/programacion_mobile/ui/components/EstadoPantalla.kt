package com.example.programacion_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EstadoPantalla(
    titulo: String,
    mensaje: String,
    modifier: Modifier = Modifier,
    cargando: Boolean = false,
    textoAccion: String? = null,
    onAccion: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (cargando) {
            CircularProgressIndicator()
        }

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        if (textoAccion != null && onAccion != null) {
            Button(onClick = onAccion) {
                Text(textoAccion)
            }
        }
    }
}
