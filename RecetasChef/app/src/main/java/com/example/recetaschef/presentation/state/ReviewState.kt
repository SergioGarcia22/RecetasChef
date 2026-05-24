package com.example.recetaschef.presentation.state

data class ReviewState(
    val comensal: String = "",
    val comentario: String = "",
    val puntuacion: Float = 0f,
    val porcionesUsados: Int = 2,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)