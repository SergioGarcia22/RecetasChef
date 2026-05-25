package com.example.recetaschef.data.model

import com.google.gson.annotations.SerializedName

// DTO para la lista general GET /recetas
data class RecipeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("tiempo_preparacion") val tiempoPreparacion: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("puntuacion_promedio") val puntuacionPromedio: Double
)

// DTO para el detalle GET /recetas/:id
data class RecipeDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("tiempo_preparacion") val tiempoPreparacion: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("ingredientes") val ingredientes: List<String>,
    @SerializedName("pasos") val pasos: List<String>,
    @SerializedName("puntuacion_promedio") val puntuacionPromedio: Double,
    @SerializedName("veces_preparada") val vecesPreparada: Int,
    @SerializedName("opiniones") val opiniones: List<ReviewDto>
)

// DTO para las reseñas
data class ReviewDto(
    @SerializedName("id") val id: Int,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("puntuacion") val puntuacion: Int
)

// DTO para estadísticas GET /recetas/:id/stats
data class RecipeStatsDto(
    @SerializedName("receta_id") val recetaId: Int,
    @SerializedName("puntuacion_promedio") val puntuacionPromedio: Double,
    @SerializedName("veces_preparada") val vecesPreparada: Int,
    @SerializedName("total_opiniones") val totalOpiniones: Int
)

// Body para POST /recetas
data class CreateRecipeRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("tiempo_preparacion") val tiempoPreparacion: Int,
    @SerializedName("ingredientes") val ingredientes: List<String>,
    @SerializedName("pasos") val pasos: List<String>
)

// Body para POST /recetas/:id/opiniones
data class CreateReviewRequest(
    @SerializedName("comentario") val comentario: String,
    @SerializedName("puntuacion") val puntuacion: Int
)
