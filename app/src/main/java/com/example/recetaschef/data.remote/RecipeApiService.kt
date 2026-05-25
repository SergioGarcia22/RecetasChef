package com.example.recetaschef.data.remote

import com.example.recetaschef.data.model.CreateRecipeRequest
import com.example.recetaschef.data.model.CreateReviewRequest
import com.example.recetaschef.data.model.RecipeDetailDto
import com.example.recetaschef.data.model.RecipeDto
import com.example.recetaschef.data.model.RecipeStatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApiService {

    // Lista general de recetas
    @GET("recetas")
    suspend fun getRecipes(): List<RecipeDto>

    // Detalle de una receta por ID
    @GET("recetas/{id}")
    suspend fun getRecipeDetail(@Path("id") id: Int): RecipeDetailDto

    // Estadísticas de una receta
    @GET("recetas/{id}/stats")
    suspend fun getRecipeStats(@Path("id") id: Int): RecipeStatsDto

    // Crear receta
    @POST("recetas")
    suspend fun postRecipe(@Body body: CreateRecipeRequest)

    // Subir opinión/puntuación de una receta
    @POST("recetas/{id}/opiniones")
    suspend fun postReview(
        @Path("id") recetaId: Int,
        @Body body: CreateReviewRequest
    )
}
