package com.example.programacion_mobile

import com.example.programacion_mobile.data.InMemoryRecordatorioRepository
import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RepositorioTest {

    @Test
    fun cargaRecordatoriosIniciales() = runBlocking {
        val repo = InMemoryRecordatorioRepository()

        assertEquals(
            RecordatoriosSimulados.recordatorios,
            repo.obtenerTodos()
        )
    }

    @Test
    fun buscaRecordatorioPorId() = runBlocking {
        val repo = InMemoryRecordatorioRepository()

        assertEquals(1, repo.obtenerPorId(1)?.id)
        assertNull(repo.obtenerPorId(999))
    }

    @Test
    fun creaRecordatorioCorrectamente() = runBlocking {
        val repo = InMemoryRecordatorioRepository()
        val cantidad = repo.obtenerTodos().size

        val nuevo = repo.crear(
            "  Nuevo recordatorio  ",
            "Descripcion de prueba",
            RecordatoriosSimulados.categorias.first(),
            Prioridad.Alta
        )

        assertEquals("Nuevo recordatorio", nuevo.titulo)
        assertEquals(Estado.Pendiente, nuevo.estado)
        assertEquals(cantidad + 1, repo.obtenerTodos().size)
        assertEquals(nuevo, repo.obtenerPorId(nuevo.id))
    }

    @Test
    fun simulaListaVacia() = runBlocking {
        val repo = InMemoryRecordatorioRepository()
        repo.simularListaVacia = true

        assertTrue(repo.obtenerTodos().isEmpty())
    }

    @Test
    fun simulaErrorDeCarga() = runBlocking {
        val repo = InMemoryRecordatorioRepository()
        repo.simularErrorCarga = true

        val resultado = runCatching {
            repo.obtenerTodos()
        }

        assertTrue(resultado.isFailure)

        repo.simularErrorCarga = false
        assertTrue(repo.obtenerTodos().isNotEmpty())
    }

    @Test
    fun simulaErrorDeGuardado() = runBlocking {
        val repo = InMemoryRecordatorioRepository()
        val cantidad = repo.obtenerTodos().size

        repo.simularErrorGuardado = true

        val resultado = runCatching {
            repo.crear(
                "Recordatorio de prueba",
                "",
                RecordatoriosSimulados.categorias.first(),
                Prioridad.Media
            )
        }

        assertTrue(resultado.isFailure)
        assertEquals(cantidad, repo.obtenerTodos().size)
    }

    @Test
    fun rechazaTituloInvalido() = runBlocking {
        val repo = InMemoryRecordatorioRepository()

        val resultado = runCatching {
            repo.crear(
                "abc",
                "",
                RecordatoriosSimulados.categorias.first(),
                Prioridad.Baja
            )
        }

        assertTrue(resultado.isFailure)
    }

    @Test
    fun rechazaDescripcionExtensa() = runBlocking {
        val repo = InMemoryRecordatorioRepository()

        val resultado = runCatching {
            repo.crear(
                "Titulo valido",
                "a".repeat(201),
                RecordatoriosSimulados.categorias.first(),
                Prioridad.Alta
            )
        }

        assertTrue(resultado.isFailure)
    }
}
