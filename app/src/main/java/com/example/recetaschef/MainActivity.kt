package com.example.recetaschef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.presentation.screens.home.HomeScreen
import com.example.recetaschef.presentation.state.RecipeUiState
import com.example.recetaschef.presentation.navigation.NavGraph
import com.example.recetaschef.ui.theme.RecetasChefTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecetasChefTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Variable para controlar qué pantalla mostrar
                    var showMockHome by remember { mutableStateOf(true) }

                    if (showMockHome) {

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
                            onRecipeClick = {
                                // Cambiar a tu navegación cuando se haga clic
                                showMockHome = false
                            },
                            onSearchChange = { query = it },
                            onAddRecipeClick = { }
                        )
                    } else {
                        // navegación con MVVM
                        NavGraph()
                    }
                }
            }
        }
    }
}