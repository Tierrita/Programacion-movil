package com.example.programacion_mobile.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.ui.components.CampoFormulario
import com.example.programacion_mobile.ui.components.SelectorFormulario
import com.example.programacion_mobile.viewmodel.FormularioUiState
import com.example.programacion_mobile.viewmodel.RecordatoriosViewModel

@Composable
fun PantallaCrearRecordatorio(
    vm: RecordatoriosViewModel,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val estado by vm.formulario.collectAsStateWithLifecycle()

    LaunchedEffect(estado.guardado) {
        if (estado.guardado) {
            vm.limpiarFormulario()
            onGuardar()
        }
    }

    val cancelar: () -> Unit = {
        vm.limpiarFormulario()
        onCancelar()
    }

    BackHandler {
        if (!estado.guardando && !estado.guardado) {
            cancelar()
        }
    }

    FormularioRecordatorio(
        estado = estado,
        onTitulo = vm::cambiarTitulo,
        onDescripcion = vm::cambiarDescripcion,
        onCategoria = vm::cambiarCategoria,
        onPrioridad = vm::cambiarPrioridad,
        onGuardar = vm::guardarRecordatorio,
        onCancelar = cancelar,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRecordatorio(
    estado: FormularioUiState,
    onTitulo: (String) -> Unit,
    onDescripcion: (String) -> Unit,
    onCategoria: (Categoria) -> Unit,
    onPrioridad: (Prioridad) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habilitado = !estado.guardando && !estado.guardado

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nuevo recordatorio") },
                navigationIcon = {
                    TextButton(
                        onClick = onCancelar,
                        enabled = habilitado,
                    ) {
                        Text("Volver")
                    }
                },
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Datos del recordatorio",
                style = MaterialTheme.typography.headlineSmall,
            )

            Text("Completa los campos obligatorios.")

            CampoFormulario(
                valor = estado.titulo,
                etiqueta = "Título *",
                onValueChange = onTitulo,
                error = estado.errorTitulo,
                habilitado = habilitado,
            )

            CampoFormulario(
                valor = estado.descripcion,
                etiqueta = "Descripción",
                onValueChange = onDescripcion,
                error = estado.errorDescripcion,
                habilitado = habilitado,
                multilinea = true,
                contador = "${estado.descripcion.length}/200",
            )

            SelectorFormulario(
                titulo = "Categoría *",
                opciones = RecordatoriosSimulados.categorias.toList(),
                seleccionado = estado.categoria,
                etiqueta = {
                    RecordatoriosSimulados.etiquetas.getValue(it)
                },
                onSeleccionar = onCategoria,
                error = estado.errorCategoria,
                habilitado = habilitado,
            )

            SelectorFormulario(
                titulo = "Prioridad *",
                opciones = listOf(
                    Prioridad.Baja,
                    Prioridad.Media,
                    Prioridad.Alta,
                ),
                seleccionado = estado.prioridad,
                etiqueta = { it.etiqueta },
                onSeleccionar = onPrioridad,
                error = estado.errorPrioridad,
                habilitado = habilitado,
            )

            estado.errorGeneral?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                onClick = onGuardar,
                enabled = estado.puedeGuardar,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )

                    Spacer(Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text("Guardar recordatorio")
                }
            }

            OutlinedButton(
                onClick = onCancelar,
                enabled = habilitado,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Cancelar")
            }
        }
    }
}
