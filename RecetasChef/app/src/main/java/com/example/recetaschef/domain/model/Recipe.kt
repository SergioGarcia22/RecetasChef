package com.example.recetaschef.domain.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    @SerializedName("tiempo_preparacion")
    val tiempoPreparacion: Int = 0,
    val porciones: Int = 2,
    val dificultad: String = "Fácil",
    @SerializedName("foto_url")
    val fotoUrl: String = "",
    @SerializedName("creado_en")
    val creadoEn: String = "",
    @SerializedName("puntuacion_promedio")
    val puntuacionPromedio: Float = 0f,
    @SerializedName("total_opiniones")
    val totalOpiniones: Int = 0,
    @SerializedName("veces_preparada")
    val vecesPreparada: Int = 0,
    val ingredientes: List<Ingredient> = emptyList(),
    val pasos: List<Step> = emptyList(),
    val opiniones: List<Review> = emptyList()
)