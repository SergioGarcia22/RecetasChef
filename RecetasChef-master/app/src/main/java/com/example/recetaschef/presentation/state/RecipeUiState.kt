package com.example.recetaschef.presentation.state

import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.Stats

data class RecipeUiState(
    val recetas: List<Recipe> = emptyList(),
    val recetaDetalle: Recipe? = null,
    val stats: Stats? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)