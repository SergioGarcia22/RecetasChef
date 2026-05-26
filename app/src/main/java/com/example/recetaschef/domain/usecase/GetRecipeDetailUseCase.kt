package com.example.recetaschef.domain.usecase

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.repository.RecipeRepository

class GetRecipeDetailUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: Int): Recipe = repository.getRecipeById(recipeId)
}