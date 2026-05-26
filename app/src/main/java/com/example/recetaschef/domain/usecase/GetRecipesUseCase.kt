package com.example.recetaschef.domain.usecase

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.repository.RecipeRepository

class GetRecipesUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(): List<Recipe> = repository.getRecipes()
}