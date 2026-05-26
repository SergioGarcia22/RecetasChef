package com.example.recetaschef.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recetaschef.data.repository.RecipeRepositoryImpl
import com.example.recetaschef.data.remote.RetrofitClient
import com.example.recetaschef.presentation.screens.detail.DetailScreen
import com.example.recetaschef.presentation.screens.home.HomeScreen
import com.example.recetaschef.presentation.viewmodel.DetailViewModel
import com.example.recetaschef.presentation.viewmodel.HomeViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val repository = RecipeRepositoryImpl(RetrofitClient.apiService)

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            val uiState by viewModel.uiState.collectAsState()

            if (uiState.isLoading && uiState.recetas.isEmpty() && uiState.errorMessage == null) {
                viewModel.loadRecipes()
            }

            HomeScreen(
                uiState = uiState,
                onRecipeClick = { recipeId ->
                    navController.navigate("detail/$recipeId")
                },
                onSearchChange = { query ->
                    viewModel.updateSearchQuery(query)
                },
                onAddRecipeClick = { }
            )
        }

        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1

            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(repository, recipeId)
            )
            val uiState by viewModel.uiState.collectAsState()

            if (uiState.isLoading && uiState.recetaDetalle == null && uiState.errorMessage == null) {
                viewModel.loadRecipeDetail()
            }

            DetailScreen(
                uiState = uiState,
                onNavigateToStats = { },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}