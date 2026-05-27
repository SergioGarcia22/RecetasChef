package com.example.recetaschef.domain.model.request

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    val comensal: String,
    val comentario: String,
    val puntuacion: Float,
    @SerializedName("porciones_usadas")
    val porcionesUsados: Int
)