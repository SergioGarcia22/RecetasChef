package com.example.recetaschef.domain.model

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int = 0,
    @SerializedName("receta_id")
    val recetaId: Int = 0,
    val comensal: String = "Anónimo",
    val comentario: String = "",
    val puntuacion: Float = 0f,
    @SerializedName("porciones_usadas")
    val porcionesUsados: Int = 2,
    val fecha: String = ""
)