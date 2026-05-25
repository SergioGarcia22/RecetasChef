package com.example.recetaschef.data.repository

import com.example.recetaschef.data.model.CreateRecipeRequest
import com.example.recetaschef.data.model.CreateReviewRequest
import com.example.recetaschef.data.model.RecipeDetailDto
import com.example.recetaschef.data.model.RecipeDto
import com.example.recetaschef.data.model.RecipeStatsDto
import com.example.recetaschef.data.model.ReviewDto
import com.example.recetaschef.data.remote.RecipeApiService
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.RecipeDetail
import com.example.recetaschef.domain.model.RecipeStats
import com.example.recetaschef.domain.model.Review
import com.example.recetaschef.domain.repository.RecipeRepository

class RecipeRepositoryImpl(
    private val apiService: RecipeApiService
) : RecipeRepository {

    // Obtiene la lista de recetas y la convierte a modelos de dominio
    override suspend fun getRecipes(): List<Recipe> {
        return apiService.getRecipes().map { it.toDomain() }
    }

    // Obtiene el detalle completo de una receta
    override suspend fun getRecipeDetail(id: Int): RecipeDetail {
        return apiService.getRecipeDetail(id).toDomain()
    }

    // Obtiene estadísticas de una receta
    override suspend fun getRecipeStats(id: Int): RecipeStats {
        return apiService.getRecipeStats(id).toDomain()
    }

    // Crea una nueva receta en el backend
    override suspend fun postRecipe(
        nombre: String,
        categoria: String,
        tiempoPreparacion: Int,
        ingredientes: List<String>,
        pasos: List<String>
    ) {
        apiService.postRecipe(
            CreateRecipeRequest(nombre, categoria, tiempoPreparacion, ingredientes, pasos)
        )
    }

    // Sube una reseña/puntuación para una receta
    override suspend fun postReview(recetaId: Int, comentario: String, puntuacion: Int) {
        apiService.postReview(recetaId, CreateReviewRequest(comentario, puntuacion))
    }

    // --- Mappers: DTO → Dominio ---

    private fun RecipeDto.toDomain() = Recipe(
        id = id,
        nombre = nombre,
        categoria = categoria,
        tiempoPreparacion = tiempoPreparacion,
        imagen = imagen,
        puntuacionPromedio = puntuacionPromedio
    )

    private fun RecipeDetailDto.toDomain() = RecipeDetail(
        id = id,
        nombre = nombre,
        categoria = categoria,
        tiempoPreparacion = tiempoPreparacion,
        imagen = imagen,
        ingredientes = ingredientes,
        pasos = pasos,
        puntuacionPromedio = puntuacionPromedio,
        vecesPreparada = vecesPreparada,
        opiniones = opiniones.map { it.toDomain() }
    )

    private fun ReviewDto.toDomain() = Review(
        id = id,
        comentario = comentario,
        puntuacion = puntuacion
    )

    private fun RecipeStatsDto.toDomain() = RecipeStats(
        recetaId = recetaId,
        puntuacionPromedio = puntuacionPromedio,
        vecesPreparada = vecesPreparada,
        totalOpiniones = totalOpiniones
    )
}
