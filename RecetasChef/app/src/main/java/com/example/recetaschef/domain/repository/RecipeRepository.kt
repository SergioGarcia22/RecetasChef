package com.example.recetaschef.domain.repository

import com.example.recetaschef.data.network.RetrofitClient
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.Stats
import com.example.recetaschef.domain.model.request.IngredienteRequest
import com.example.recetaschef.domain.model.request.PasoRequest
import com.example.recetaschef.domain.model.request.RecipeRequest
import com.example.recetaschef.domain.model.request.ReviewRequest

class RecipeRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAllRecipes(): List<Recipe> {
        val response = api.getRecipes()
        return if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        val response = api.getRecipeById(id)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getStats(id: Int): Stats? {
        val response = api.getStats(id)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun saveRecipe(request: RecipeRequest): Boolean {
        val response = api.createRecipe(request)
        return response.isSuccessful
    }

    suspend fun addReview(recipeId: Int, request: ReviewRequest): Boolean {
        val response = api.addReview(recipeId, request)
        return response.isSuccessful
    }
}