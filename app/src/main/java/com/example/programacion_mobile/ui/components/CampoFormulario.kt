package com.example.programacion_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CampoFormulario(
    valor: String,
    etiqueta: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    habilitado: Boolean = true,
    multilinea: Boolean = false,
    contador: String? = null,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        enabled = habilitado,
        isError = error != null,
        singleLine = !multilinea,
        minLines = if (multilinea) 3 else 1,
        supportingText = {
            Column {
                if (error != null) {
                    Text(error)
                }

                if (contador != null) {
                    Text(contador)
                }
            }
        },
    )
}
