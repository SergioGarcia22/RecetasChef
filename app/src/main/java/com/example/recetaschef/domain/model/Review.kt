package com.example.recetaschef.domain.model

data class Review(
    val id: Int,
    val recipeId: Int,
    val comentario: String,
    val puntuacion: Int,
    val porciones: Int,
    val fecha: String
)