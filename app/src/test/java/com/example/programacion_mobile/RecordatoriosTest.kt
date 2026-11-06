package com.example.programacion_mobile

import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RecordatoriosTest {
    private val datos = RecordatoriosSimulados

    @Test
    fun rechazaTitulosEnBlanco() {
        assertThrows(IllegalArgumentException::class.java) {
            datos.recordatorios.first().copy(titulo = "   ")
        }
    }

    @Test
    fun rechazaIdentificadoresNoPositivos() {
        assertThrows(IllegalArgumentException::class.java) {
            datos.recordatorios.first().copy(id = 0)
        }
    }

    @Test
    fun categoriasSinDuplicadosYConEtiqueta() {
        assertEquals(3, datos.categorias.size)
        assertEquals(datos.categorias, datos.etiquetas.keys)
    }

    @Test
    fun aplicaFiltrosPorSeparadoYCombinados() {
        assertEquals(listOf(1, 2, 3, 4), datos.filtrar().map { it.id })
        assertEquals(listOf(1, 2, 3), datos.filtrar(estado = Estado.Pendiente).map { it.id })
        assertEquals(listOf(1, 4), datos.filtrar(prioridad = Prioridad.Alta).map { it.id })
        assertEquals(
            listOf(1),
            datos.filtrar(estado = Estado.Pendiente, prioridad = Prioridad.Alta).map { it.id },
        )
    }
}
