package com.example.recetaschef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recetaschef.domain.repository.RecipeRepository
import com.example.recetaschef.presentation.AppNavigation
import com.example.recetaschef.presentation.RecipeViewModel
import com.example.recetaschef.presentation.RecipeViewModelFactory
import com.example.recetaschef.ui.theme.RecetasChefTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = RecipeRepository()
        val factory    = RecipeViewModelFactory(repository)

        setContent {
            RecetasChefTheme {
                val viewModel: RecipeViewModel = viewModel(factory = factory)
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}