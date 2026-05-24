package com.example.recetaschef.presentation.state

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.Stats

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList(),
    val selectedRecipe: Recipe? = null,
    val stats: Stats? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)