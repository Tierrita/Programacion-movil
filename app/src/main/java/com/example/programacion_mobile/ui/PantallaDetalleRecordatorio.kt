package com.example.programacion_mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.ui.components.EstadoPantalla
import com.example.programacion_mobile.viewmodel.DetalleUiState
import com.example.programacion_mobile.viewmodel.RecordatoriosViewModel

@Composable
fun PantallaDetalleRecordatorio(
    id: Int,
    vm: RecordatoriosViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        vm.cargarDetalle(id)
    }

    DetalleRecordatorio(
        estado = estado,
        onVolver = onVolver,
        onReintentar = { vm.cargarDetalle(id) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecordatorio(
    estado: DetalleUiState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                },
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            when (estado) {
                DetalleUiState.Cargando -> {
                    EstadoPantalla(
                        titulo = "Cargando",
                        mensaje = "Consultando recordatorio...",
                        cargando = true,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is DetalleUiState.Error -> {
                    EstadoPantalla(
                        titulo = "Ocurrio un error",
                        mensaje = estado.mensaje,
                        textoAccion = "Reintentar",
                        onAccion = onReintentar,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is DetalleUiState.Datos -> {
                    val recordatorio = estado.recordatorio

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement =
                            Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = recordatorio.titulo,
                            style = MaterialTheme.typography
                                .headlineMedium,
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(16.dp),
                            ) {
                                DatoDetalle(
                                    "Categoria",
                                    RecordatoriosSimulados.etiquetas
                                        .getValue(recordatorio.categoria)
                                )

                                HorizontalDivider()

                                DatoDetalle(
                                    "Prioridad",
                                    recordatorio.prioridad.etiqueta,
                                )

                                HorizontalDivider()

                                DatoDetalle(
                                    "Estado",
                                    recordatorio.estado.etiqueta,
                                )

                                HorizontalDivider()

                                DatoDetalle(
                                    "Descripcion",
                                    recordatorio.descripcion
                                        ?.takeIf { it.isNotBlank() }
                                        ?: "Sin descripcion",
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onVolver,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Volver al listado")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatoDetalle(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
