package com.example.programacion_mobile.data

import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio

interface RecordatorioRepository {

    suspend fun obtenerTodos(): List<Recordatorio>

    suspend fun obtenerPorId(id: Int): Recordatorio?

    suspend fun crear(
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad,
    ): Recordatorio
}
