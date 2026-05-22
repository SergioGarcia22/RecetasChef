package com.example.recetaschef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.presentation.screens.home.HomeScreen
import com.example.recetaschef.presentation.state.RecipeUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var query by remember { mutableStateOf("") }

            val mockState = RecipeUiState(
                recetas = listOf(
                    Recipe(1, "Pasta Alfredo", "Clásica salsa cremosa.", "Italiana", listOf("200g pasta", "100ml crema"), listOf("Cocinar pasta", "Agregar salsa"), 30, 4.7, 5, emptyList()),
                    Recipe(2, "Pollo al horno", "Pollo con finas herbs.", "Carnes", listOf("1 pollo", "Especias"), listOf("Sazonar", "Hornear 45 min"), 45, 4.2, 3, emptyList()),
                    Recipe(3, "Brownies", "Postre de chocolate.", "Postres", listOf("Harina", "Chocolate"), listOf("Mezclar", "Hornear 20 min"), 25, 4.9, 12, emptyList())
                ),
                searchQuery = query
            )

            HomeScreen(
                uiState = mockState,
                onRecipeClick = { },
                onSearchChange = { query = it },
                onAddRecipeClick = { }
            )
        }
    }
}