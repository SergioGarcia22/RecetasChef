package com.example.recetaschef.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recetaschef.presentation.screens.home.HomeScreen
import com.example.recetaschef.presentation.screens.detail.DetailScreen
import com.example.recetaschef.presentation.screens.add.AddRecipeScreen

// Rutas de navegación
object Routes {
    const val HOME   = "home"
    const val DETAIL = "detail/{recipeId}"
    const val ADD    = "add"

    fun detail(recipeId: Int) = "detail/$recipeId"
}

@Composable
fun AppNavigation(viewModel: RecipeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel  = viewModel,
                onNavigateToDetail = { recipeId ->
                    navController.navigate(Routes.detail(recipeId))
                },
                onNavigateToAdd = {
                    navController.navigate(Routes.ADD)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
            DetailScreen(
                viewModel  = viewModel,
                recipeId   = recipeId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADD) {
            AddRecipeScreen(
                viewModel      = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}