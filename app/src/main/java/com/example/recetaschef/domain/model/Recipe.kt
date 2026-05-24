package com.example.recetaschef.domain.model

data class Recipe(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val categoria: String,
    val ingredientes: List<String>,
    val preparacion: List<String>,
    val tiempoPreparacion: Int,
    val puntuacionPromedio: Double,
    val vecesPreparada: Int,
    val opiniones: List<Review>
)