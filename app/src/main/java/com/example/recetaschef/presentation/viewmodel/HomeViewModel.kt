package com.example.recetaschef.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetaschef.domain.repository.RecipeRepository
import com.example.recetaschef.presentation.state.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUiState(isLoading = true))
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = RecipeUiState(isLoading = true)
            try {
                val recipes = repository.getRecipes()
                _uiState.value = RecipeUiState(
                    recetas = recipes,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = RecipeUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al cargar recetas"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun retryLoad() {
        loadRecipes()
    }
}