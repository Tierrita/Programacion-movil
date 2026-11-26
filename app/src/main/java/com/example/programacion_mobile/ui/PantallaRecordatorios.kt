package com.example.programacion_mobile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.ui.components.EstadoPantalla
import com.example.programacion_mobile.ui.components.TarjetaRecordatorio
import com.example.programacion_mobile.viewmodel.RecordatoriosUiState
import com.example.programacion_mobile.viewmodel.RecordatoriosViewModel

@Composable
fun PantallaRecordatorios(
    modifier: Modifier = Modifier,
    onCrearRecordatorio: () -> Unit = {},
    onRecordatorioClick: (Int) -> Unit = {},
    vm: RecordatoriosViewModel = viewModel(),
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    ListadoRecordatorios(
        estado = estado,
        onCrearRecordatorio = onCrearRecordatorio,
        onRecordatorioClick = onRecordatorioClick,
        onReintentar = vm::reintentar,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoRecordatorios(
    estado: RecordatoriosUiState,
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var soloPendientes by rememberSaveable {
        mutableStateOf(false)
    }

    var soloPrioridadAlta by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCrearRecordatorio,
                text = { Text("Nuevo recordatorio") },
                icon = { Text("+") },
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            when (estado) {
                RecordatoriosUiState.Cargando -> {
                    EstadoPantalla(
                        titulo = "Cargando",
                        mensaje = "Consultando tus recordatorios...",
                        cargando = true,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                RecordatoriosUiState.Vacio -> {
                    EstadoPantalla(
                        titulo = "No hay recordatorios",
                        mensaje = "Crea tu primer recordatorio.",
                        textoAccion = "Crear recordatorio",
                        onAccion = onCrearRecordatorio,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is RecordatoriosUiState.Error -> {
                    EstadoPantalla(
                        titulo = "Ocurrió un error",
                        mensaje = estado.mensaje,
                        textoAccion = "Reintentar",
                        onAccion = onReintentar,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is RecordatoriosUiState.Datos -> {
                    val filtrados = estado.recordatorios.filter {
                        (!soloPendientes ||
                            it.estado == Estado.Pendiente) &&
                        (!soloPrioridadAlta ||
                            it.prioridad == Prioridad.Alta)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            top = 12.dp,
                            bottom = 96.dp,
                        ),
                    ) {
                        item {
                            Text(
                                "Organiza tus pendientes",
                                style = MaterialTheme.typography
                                    .headlineSmall,
                            )

                            Text(
                                "${estado.recordatorios.size} recordatorios"
                            )

                            Row(
                                modifier = Modifier.horizontalScroll(
                                    rememberScrollState()
                                ),
                                horizontalArrangement =
                                    Arrangement.spacedBy(8.dp),
                            ) {
                                FilterChip(
                                    selected = soloPendientes,
                                    onClick = {
                                        soloPendientes = !soloPendientes
                                    },
                                    label = { Text("Pendientes") },
                                )

                                FilterChip(
                                    selected = soloPrioridadAlta,
                                    onClick = {
                                        soloPrioridadAlta =
                                            !soloPrioridadAlta
                                    },
                                    label = { Text("Prioridad alta") },
                                )
                            }
                        }

                        if (filtrados.isEmpty()) {
                            item {
                                EstadoPantalla(
                                    titulo = "Sin resultados",
                                    mensaje = "No hay coincidencias.",
                                    textoAccion = "Limpiar filtros",
                                    onAccion = {
                                        soloPendientes = false
                                        soloPrioridadAlta = false
                                    },
                                )
                            }
                        }

                        items(filtrados, key = { it.id }) {
                            recordatorio ->
                            TarjetaRecordatorio(
                                recordatorio = recordatorio,
                                onClick = {
                                    onRecordatorioClick(recordatorio.id)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
