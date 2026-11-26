package com.example.programacion_mobile

import com.example.programacion_mobile.data.InMemoryRecordatorioRepository
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RecordatoriosViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun finalizar() {
        Dispatchers.resetMain()
    }

    private fun completarFormulario(vm: RecordatoriosViewModel) {
        vm.cambiarTitulo("Preparar examen")
        vm.cambiarDescripcion("Repasar Kotlin")
        vm.cambiarCategoria(
            RecordatoriosSimulados.categorias.first()
        )
        vm.cambiarPrioridad(Prioridad.Alta)
    }

    @Test
    fun cargaInicialCorrecta() = runTest {
        val vm = RecordatoriosViewModel()

        assertEquals(
            RecordatoriosUiState.Cargando,
            vm.uiState.value
        )

        advanceUntilIdle()

        val estado = vm.uiState.value as RecordatoriosUiState.Datos

        assertEquals(4, estado.recordatorios.size)
    }

    @Test
    fun listaVacia() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        vm.simularVacio()
        advanceUntilIdle()

        assertEquals(
            RecordatoriosUiState.Vacio,
            vm.uiState.value
        )
    }

    @Test
    fun errorDeCargaYReintento() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        vm.simularError()
        advanceUntilIdle()

        assertTrue(
            vm.uiState.value is RecordatoriosUiState.Error
        )

        vm.reintentar()
        advanceUntilIdle()

        assertTrue(
            vm.uiState.value is RecordatoriosUiState.Datos
        )
    }

    @Test
    fun formularioInvalidoNoGuarda() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        vm.guardarRecordatorio()
        advanceUntilIdle()

        assertFalse(vm.formulario.value.guardado)

        val estado = vm.uiState.value as RecordatoriosUiState.Datos

        assertEquals(4, estado.recordatorios.size)
    }

    @Test
    fun guardadoCorrectoActualizaListado() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        completarFormulario(vm)

        assertTrue(vm.formulario.value.puedeGuardar)

        vm.guardarRecordatorio()

        assertTrue(vm.formulario.value.guardando)
        assertFalse(vm.formulario.value.puedeGuardar)

        advanceUntilIdle()

        assertTrue(vm.formulario.value.guardado)
        assertFalse(vm.formulario.value.guardando)

        val estado = vm.uiState.value as RecordatoriosUiState.Datos

        assertEquals(5, estado.recordatorios.size)
        assertTrue(
            estado.recordatorios.any {
                it.titulo == "Preparar examen"
            }
        )
    }

    @Test
    fun evitaGuardadoDuplicado() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        completarFormulario(vm)

        vm.guardarRecordatorio()
        vm.guardarRecordatorio()

        advanceUntilIdle()

        vm.guardarRecordatorio()
        advanceUntilIdle()

        val estado = vm.uiState.value as RecordatoriosUiState.Datos

        assertEquals(5, estado.recordatorios.size)
    }

    @Test
    fun errorDeGuardadoPermiteReintentar() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        completarFormulario(vm)
        vm.simularErrorGuardado(true)

        vm.guardarRecordatorio()
        advanceUntilIdle()

        assertNotNull(vm.formulario.value.errorGeneral)
        assertFalse(vm.formulario.value.guardado)
        assertFalse(vm.formulario.value.guardando)
        assertTrue(vm.formulario.value.puedeGuardar)

        val estado = vm.uiState.value as RecordatoriosUiState.Datos
        assertEquals(4, estado.recordatorios.size)

        vm.simularErrorGuardado(false)
        vm.guardarRecordatorio()
        advanceUntilIdle()

        assertTrue(vm.formulario.value.guardado)

        val actualizado = vm.uiState.value as RecordatoriosUiState.Datos
        assertEquals(5, actualizado.recordatorios.size)
    }

    @Test
    fun detalleExistente() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        vm.cargarDetalle(1)
        advanceUntilIdle()

        val estado = vm.detalle.value as DetalleUiState.Datos

        assertEquals(1, estado.recordatorio.id)
    }

    @Test
    fun detalleInexistente() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        vm.cargarDetalle(99999)
        advanceUntilIdle()

        assertTrue(
            vm.detalle.value is DetalleUiState.Error
        )
    }

    @Test
    fun limpiarFormulario() = runTest {
        val vm = RecordatoriosViewModel()

        advanceUntilIdle()
        completarFormulario(vm)
        vm.limpiarFormulario()

        assertEquals(
            FormularioUiState(),
            vm.formulario.value
        )
    }
}
