package com.example.recetaschef.domain.model

import com.google.gson.annotations.SerializedName

data class Step(
    val id: Int = 0,
    @SerializedName("receta_id")
    val recetaId: Int = 0,
    @SerializedName("numero_paso")
    val numeroPaso: Int = 0,
    val descripcion: String = ""
)