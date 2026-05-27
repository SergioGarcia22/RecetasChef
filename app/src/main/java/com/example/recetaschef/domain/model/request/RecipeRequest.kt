package com.example.recetaschef.domain.model.request

import com.google.gson.annotations.SerializedName

data class RecipeRequest(
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    @SerializedName("tiempo_preparacion")
    val tiempoPreparacion: Int,
    val porciones: Int,
    val dificultad: String,
    @SerializedName("foto_url")
    val fotoUrl: String,
    val ingredientes: List<IngredienteRequest>,
    val pasos: List<PasoRequest>
)

data class IngredienteRequest(
    val nombre: String,
    val cantidad: String,
    val unidad: String
)

data class PasoRequest(
    val descripcion: String
)