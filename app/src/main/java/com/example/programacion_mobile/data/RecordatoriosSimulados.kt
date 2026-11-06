package com.example.programacion_mobile.data

import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Estado
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio

object RecordatoriosSimulados {
    private val estudio = Categoria("estudio")
    private val personal = Categoria("personal")
    private val trabajo = Categoria("trabajo")

    val etiquetas: Map<Categoria, String> = mapOf(
        estudio to "Estudio",
        personal to "Personal",
        trabajo to "Trabajo",
    )

    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            1, "Entregar la actividad de Android", estudio, Prioridad.Alta, Estado.Pendiente,
            "Adjuntar el PDF con las evidencias.",
        ),
        Recordatorio(2, "Preparar el recorrido de reparto", trabajo, Prioridad.Media, Estado.Pendiente),
        Recordatorio(3, "Comprar para la semana", personal, Prioridad.Baja, Estado.Pendiente),
        Recordatorio(4, "Configurar Android Studio", estudio, Prioridad.Alta, Estado.Completado),
    )

    val categorias: Set<Categoria> = recordatorios.map { it.categoria }.toSet()

    fun filtrar(estado: Estado? = null, prioridad: Prioridad? = null): List<Recordatorio> =
        recordatorios.filter { recordatorio ->
            (estado == null || recordatorio.estado == estado) &&
                (prioridad == null || recordatorio.prioridad == prioridad)
        }
}
