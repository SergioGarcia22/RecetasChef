package com.example.recetaschef.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetaschef.domain.model.request.IngredienteRequest
import com.example.recetaschef.domain.model.request.PasoRequest
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.domain.model.request.RecipeRequest
import com.example.recetaschef.domain.model.request.ReviewRequest
import com.example.recetaschef.domain.repository.RecipeRepository
import com.example.recetaschef.presentation.state.RecipeUiState
import com.example.recetaschef.presentation.state.ReviewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _reviewState = MutableStateFlow(ReviewState())
    val reviewState: StateFlow<ReviewState> = _reviewState.asStateFlow()

    init { loadRecipes() }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val recipes = repository.getAllRecipes()
            _uiState.update { it.copy(recipes = recipes, isLoading = false) }
        }
    }

    fun loadRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val recipe = repository.getRecipeById(recipeId)
            _uiState.update {
                it.copy(
                    selectedRecipe = recipe,
                    isLoading      = false
                )
            }
        }
    }

    fun loadStats(recipeId: Int) {
        viewModelScope.launch {
            val stats = repository.getStats(recipeId)
            _uiState.update { it.copy(stats = stats) }
        }
    }

    fun saveRecipe(
        nombre: String,
        descripcion: String,
        categoria: String,
        tiempoPreparacion: Int,
        porciones: Int,
        dificultad: String,
        fotoUrl: String,
        ingredientes: List<IngredienteRequest>,
        pasos: List<PasoRequest>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.saveRecipe(
                RecipeRequest(
                    nombre             = nombre,
                    descripcion        = descripcion,
                    categoria          = categoria,
                    tiempoPreparacion  = tiempoPreparacion,
                    porciones          = porciones,
                    dificultad         = dificultad,
                    fotoUrl            = fotoUrl,
                    ingredientes       = ingredientes,
                    pasos              = pasos
                )
            )
            if (success) loadRecipes()
            _uiState.update {
                it.copy(
                    isLoading      = false,
                    successMessage = if (success) "Receta guardada" else null,
                    errorMessage   = if (!success) "Error al guardar" else null
                )
            }
        }
    }

    fun addReview(recipeId: Int) {
        val current = _reviewState.value
        if (current.puntuacion == 0f) {
            _reviewState.update { it.copy(errorMessage = "Selecciona una puntuación") }
            return
        }
        viewModelScope.launch {
            _reviewState.update { it.copy(isLoading = true) }
            val success = repository.addReview(
                recipeId,
                ReviewRequest(
                    comensal        = current.comensal.ifBlank { "Anónimo" },
                    comentario      = current.comentario,
                    puntuacion      = current.puntuacion,
                    porcionesUsados = current.porcionesUsados
                )
            )
            _reviewState.update {
                if (success) ReviewState(successMessage = "Opinión guardada")
                else it.copy(isLoading = false, errorMessage = "Error al guardar opinión")
            }
            if (success) loadStats(recipeId)
            loadRecipeDetail(recipeId)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            // Por ahora recarga la lista sin la receta eliminada localmente
            // Si quieres, puedes agregar un endpoint DELETE en el backend después
            _uiState.update { current ->
                current.copy(recipes = current.recipes.filter { it.id != recipe.id })
            }
        }
    }

    fun onReviewComensal(value: String)   = _reviewState.update { it.copy(comensal = value) }
    fun onReviewComentario(value: String) = _reviewState.update { it.copy(comentario = value) }
    fun onReviewPuntuacion(value: Float)  = _reviewState.update { it.copy(puntuacion = value) }
    fun onReviewPorciones(value: Int)     = _reviewState.update { it.copy(porcionesUsados = value) }

    fun clearMessages() {
        _uiState.update   { it.copy(errorMessage = null, successMessage = null) }
        _reviewState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}