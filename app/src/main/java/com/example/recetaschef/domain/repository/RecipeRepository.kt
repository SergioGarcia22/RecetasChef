package com.example.recetaschef.domain.repository

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.Review
import com.example.recetaschef.domain.model.Stats

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getRecipeById(id: Int): Recipe
    suspend fun getStats(recipeId: Int): Stats
    suspend fun addReview(review: Review): Boolean
}