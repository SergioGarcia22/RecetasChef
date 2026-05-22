package com.example.recetaschef.presentation.state

data class ReviewUiState(
    val isLoading: Boolean = false,
    val submitSuccess: Boolean = false,
    val errorMessage: String? = null
)