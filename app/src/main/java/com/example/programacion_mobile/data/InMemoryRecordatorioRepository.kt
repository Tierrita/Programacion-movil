package com.example.programacion_mobile.data

import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryRecordatorioRepository(
    iniciales: List<Recordatorio> = RecordatoriosSimulados.recordatorios,
) : RecordatorioRepository, ControlSimulaciones {

    private val mutex = Mutex()
    private val datos = iniciales.toMutableList()
    private var siguienteId = (iniciales.maxOfOrNull { it.id } ?: 0) + 1

    override var simularErrorCarga = false
    override var simularErrorGuardado = false
    override var simularListaVacia = false

    override suspend fun obtenerTodos(): List<Recordatorio> {
        delay(800)

        return mutex.withLock {
            check(!simularErrorCarga) {
                "No se pudieron cargar los recordatorios."
            }

            if (simularListaVacia) emptyList()
            else datos.toList()
        }
    }

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        return mutex.withLock {
            datos.firstOrNull { it.id == id }
        }
    }

    override suspend fun crear(
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad,
    ): Recordatorio {
        delay(1000)

        return mutex.withLock {
            check(!simularErrorGuardado) {
                "No se pudo guardar el recordatorio."
            }

            require(titulo.trim().length >= 4)
            require(descripcion.length <= 200)
            require(categoria in RecordatoriosSimulados.categorias)

            val recordatorio = Recordatorio(
                id = siguienteId++,
                titulo = titulo.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria,
                prioridad = prioridad,
                estado = Estado.Pendiente,
            )

            datos.add(recordatorio)
            simularListaVacia = false

            recordatorio
        }
    }
}
