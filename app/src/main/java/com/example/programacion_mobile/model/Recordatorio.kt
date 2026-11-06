package com.example.programacion_mobile.model

data class Categoria(val id: String)

sealed class Prioridad(val etiqueta: String) {
    object Alta : Prioridad("Alta")
    object Media : Prioridad("Media")
    object Baja : Prioridad("Baja")
}

sealed class Estado(val etiqueta: String) {
    object Pendiente : Estado("Pendiente")
    object Completado : Estado("Completado")
}

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: Estado,
    val descripcion: String? = null,
) {
    init {
        require(id > 0) { "El identificador debe ser positivo." }
        require(titulo.isNotBlank()) { "El título es obligatorio." }
    }
}
