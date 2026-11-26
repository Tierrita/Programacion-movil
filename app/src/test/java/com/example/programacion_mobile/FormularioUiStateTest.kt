package com.example.programacion_mobile

import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.viewmodel.FormularioUiState
import org.junit.Assert.*
import org.junit.Test

class FormularioUiStateTest {

    private val valido = FormularioUiState(
        titulo = "Estudiar Android",
        descripcion = "Preparar actividad",
        categoria = RecordatoriosSimulados.categorias.first(),
        prioridad = Prioridad.Alta,
    )

    @Test
    fun formularioInicialInvalido() {
        assertFalse(FormularioUiState().puedeGuardar)
    }

    @Test
    fun formularioValido() {
        assertTrue(valido.puedeGuardar)
    }

    @Test
    fun tituloMenorACuatroCaracteres() {
        assertFalse(valido.copy(titulo = "abc").puedeGuardar)
    }

    @Test
    fun tituloConCuatroCaracteres() {
        assertTrue(valido.copy(titulo = "abcd").puedeGuardar)
    }

    @Test
    fun tituloIgnoraEspaciosExternos() {
        assertFalse(valido.copy(titulo = "  abc  ").puedeGuardar)
    }

    @Test
    fun descripcionVaciaPermitida() {
        assertTrue(valido.copy(descripcion = "").puedeGuardar)
    }

    @Test
    fun descripcionDeDoscientosCaracteres() {
        assertTrue(
            valido.copy(descripcion = "a".repeat(200)).puedeGuardar
        )
    }

    @Test
    fun descripcionMayorADoscientosCaracteres() {
        assertFalse(
            valido.copy(descripcion = "a".repeat(201)).puedeGuardar
        )
    }

    @Test
    fun categoriaNoSeleccionada() {
        assertFalse(valido.copy(categoria = null).puedeGuardar)
    }

    @Test
    fun categoriaInexistente() {
        assertFalse(
            valido.copy(categoria = Categoria("inexistente")).puedeGuardar
        )
    }

    @Test
    fun prioridadNoSeleccionada() {
        assertFalse(valido.copy(prioridad = null).puedeGuardar)
    }

    @Test
    fun bloqueaGuardadoDuranteOperacion() {
        assertFalse(valido.copy(guardando = true).puedeGuardar)
    }

    @Test
    fun bloqueaGuardadoDuplicado() {
        assertFalse(valido.copy(guardado = true).puedeGuardar)
    }
}
