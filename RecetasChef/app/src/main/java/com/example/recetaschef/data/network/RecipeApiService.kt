package com.example.recetaschef.data.network

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.Stats
import com.example.recetaschef.domain.model.request.RecipeRequest
import com.example.recetaschef.domain.model.request.ReviewRequest
import retrofit2.Response
import retrofit2.http.*

interface RecipeApiService {

    @GET("recetas")
    suspend fun getRecipes(): Response<List<Recipe>>

    @GET("recetas/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Response<Recipe>

    @GET("recetas/{id}/stats")
    suspend fun getStats(@Path("id") id: Int): Response<Stats>

    @POST("recetas")
    suspend fun createRecipe(@Body recipe: RecipeRequest): Response<Map<String, Any>>

    @POST("recetas/{id}/opiniones")
    suspend fun addReview(
        @Path("id") id: Int,
        @Body review: ReviewRequest
    ): Response<Map<String, Any>>
}