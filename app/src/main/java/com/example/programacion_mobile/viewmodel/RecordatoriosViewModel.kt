package com.example.programacion_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programacion_mobile.data.InMemoryRecordatorioRepository
import com.example.programacion_mobile.data.RecordatorioRepository
import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Prioridad
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordatoriosViewModel(
    private val repository: RecordatorioRepository =
        InMemoryRecordatorioRepository(),
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<RecordatoriosUiState>(
            RecordatoriosUiState.Cargando
        )

    val uiState = _uiState.asStateFlow()

    private val _formulario =
        MutableStateFlow(FormularioUiState())

    val formulario = _formulario.asStateFlow()

    private var cargaJob: Job? = null

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        cargaJob?.cancel()

        cargaJob = viewModelScope.launch {
            _uiState.value = RecordatoriosUiState.Cargando

            try {
                val datos = repository.obtenerTodos()

                _uiState.value = if (datos.isEmpty()) {
                    RecordatoriosUiState.Vacio
                } else {
                    RecordatoriosUiState.Datos(datos.toList())
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = RecordatoriosUiState.Error(
                    e.message ?: "Error al cargar recordatorios."
                )
            }
        }
    }

    fun reintentar() {
        cargarRecordatorios()
    }

    fun cambiarTitulo(valor: String) {
        actualizarFormulario {
            it.copy(titulo = valor, errorGeneral = null)
        }
    }

    fun cambiarDescripcion(valor: String) {
        actualizarFormulario {
            it.copy(descripcion = valor, errorGeneral = null)
        }
    }

    fun cambiarCategoria(valor: Categoria) {
        actualizarFormulario {
            it.copy(categoria = valor, errorGeneral = null)
        }
    }

    fun cambiarPrioridad(valor: Prioridad) {
        actualizarFormulario {
            it.copy(prioridad = valor, errorGeneral = null)
        }
    }

    private fun actualizarFormulario(
        transformar: (FormularioUiState) -> FormularioUiState,
    ) {
        _formulario.update {
            if (it.guardando || it.guardado) it
            else transformar(it)
        }
    }

    fun limpiarFormulario() {
        if (!_formulario.value.guardando) {
            _formulario.value = FormularioUiState()
        }
    }

    fun guardarRecordatorio() {
        val actual = _formulario.value

        if (!actual.puedeGuardar) return

        val categoria = actual.categoria ?: return
        val prioridad = actual.prioridad ?: return

        _formulario.update {
            it.copy(guardando = true, errorGeneral = null)
        }

        viewModelScope.launch {
            try {
                repository.crear(
                    titulo = actual.titulo.trim(),
                    descripcion = actual.descripcion,
                    categoria = categoria,
                    prioridad = prioridad,
                )

                _formulario.update {
                    it.copy(
                        guardando = false,
                        guardado = true,
                    )
                }

                cargarRecordatorios()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _formulario.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = e.message
                            ?: "No se pudo guardar el recordatorio.",
                    )
                }
            }
        }
    }
}
