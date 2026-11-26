package com.example.programacion_mobile.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MenuSimulaciones(
    onDatos: () -> Unit,
    onVacio: () -> Unit,
    onError: () -> Unit,
    onErrorGuardado: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var abierto by remember { mutableStateOf(false) }

    TextButton(
        onClick = { abierto = true },
        modifier = modifier,
    ) {
        Text("Pruebas")
    }

    DropdownMenu(
        expanded = abierto,
        onDismissRequest = { abierto = false },
    ) {
        DropdownMenuItem(
            text = { Text("Mostrar datos") },
            onClick = {
                abierto = false
                onDatos()
            },
        )

        DropdownMenuItem(
            text = { Text("Simular lista vacía") },
            onClick = {
                abierto = false
                onVacio()
            },
        )

        DropdownMenuItem(
            text = { Text("Simular error de carga") },
            onClick = {
                abierto = false
                onError()
            },
        )

        DropdownMenuItem(
            text = { Text("Activar error de guardado") },
            onClick = {
                abierto = false
                onErrorGuardado(true)
            },
        )

        DropdownMenuItem(
            text = { Text("Desactivar error de guardado") },
            onClick = {
                abierto = false
                onErrorGuardado(false)
            },
        )
    }
}
