package com.example.recetaschef.domain.model

data class Stats(
    val recipeId: Int,
    val puntuacionPromedio: Double,
    val totalPreparaciones: Int,
    val porcionesPromedio: Double,
    val tiempoPromedio: Int,
    val historial: List<Review>
)