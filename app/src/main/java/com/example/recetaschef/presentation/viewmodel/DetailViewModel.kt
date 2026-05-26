package com.example.recetaschef.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetaschef.domain.repository.RecipeRepository
import com.example.recetaschef.presentation.state.RecipeUiState
import com.example.recetaschef.presentation.state.ReviewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(
    private val repository: RecipeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Int = savedStateHandle["recipeId"] ?: -1

    private val _uiState = MutableStateFlow(RecipeUiState(isLoading = true))
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _reviewState = MutableStateFlow(ReviewUiState())
    val reviewState: StateFlow<ReviewUiState> = _reviewState.asStateFlow()

    fun loadRecipeDetail() {
        viewModelScope.launch {
            _uiState.value = RecipeUiState(isLoading = true)
            try {
                val recipe = repository.getRecipeById(recipeId)
                val stats = repository.getStats(recipeId)
                _uiState.value = RecipeUiState(
                    recetaDetalle = recipe,
                    stats = stats,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = RecipeUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al cargar detalle"
                )
            }
        }
    }

    fun submitReview(comment: String, rating: Int) {
        viewModelScope.launch {
            _reviewState.value = ReviewUiState(isLoading = true)
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val review = com.example.recetaschef.domain.model.Review(
                    id = 0,
                    recipeId = recipeId,
                    comentario = comment,
                    puntuacion = rating,
                    porciones = 1,  // Valor por defecto
                    fecha = currentDate
                )
                val success = repository.addReview(review)
                if (success) {
                    _reviewState.value = ReviewUiState(submitSuccess = true)
                    loadRecipeDetail()
                } else {
                    _reviewState.value = ReviewUiState(errorMessage = "No se pudo enviar la opinión")
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewUiState(errorMessage = e.message ?: "Error al enviar")
            }
        }
    }

    fun resetReviewState() {
        _reviewState.value = ReviewUiState()
    }
}