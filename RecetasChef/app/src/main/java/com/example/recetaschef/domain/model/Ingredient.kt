package com.example.recetaschef.domain.model

import com.google.gson.annotations.SerializedName

data class Ingredient(
    val id: Int = 0,
    @SerializedName("receta_id")
    val recetaId: Int = 0,
    val nombre: String = "",
    val cantidad: String = "",
    val unidad: String = ""
)