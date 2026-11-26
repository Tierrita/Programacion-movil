package com.example.programacion_mobile.viewmodel

import com.example.programacion_mobile.data.RecordatoriosSimulados
import com.example.programacion_mobile.model.Categoria
import com.example.programacion_mobile.model.Prioridad
import com.example.programacion_mobile.model.Recordatorio

sealed interface RecordatoriosUiState {

    data object Cargando : RecordatoriosUiState

    data object Vacio : RecordatoriosUiState

    data class Datos(
        val recordatorios: List<Recordatorio>,
    ) : RecordatoriosUiState

    data class Error(
        val mensaje: String,
    ) : RecordatoriosUiState
}

data class FormularioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,
    val prioridad: Prioridad? = null,
    val guardando: Boolean = false,
    val guardado: Boolean = false,
    val errorGeneral: String? = null,
) {
    val errorTitulo: String?
        get() = if (titulo.trim().length < 4)
            "El titulo debe tener al menos 4 caracteres."
        else null

    val errorDescripcion: String?
        get() = if (descripcion.length > 200)
            "La descripcion no puede superar los 200 caracteres."
        else null

    val errorCategoria: String?
        get() = if (categoria !in RecordatoriosSimulados.categorias)
            "Selecciona una categoria valida."
        else null

    val errorPrioridad: String?
        get() = if (prioridad == null)
            "Selecciona una prioridad."
        else null

    val puedeGuardar: Boolean
        get() = errorTitulo == null &&
            errorDescripcion == null &&
            errorCategoria == null &&
            errorPrioridad == null &&
            !guardando &&
            !guardado
}
