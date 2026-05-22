package com.example.recetaschef.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.presentation.components.LoadingView
import com.example.recetaschef.presentation.components.RecipeCard
import com.example.recetaschef.presentation.components.TopBar
import com.example.recetaschef.presentation.state.RecipeUiState

@Composable
fun HomeScreen(
    uiState: RecipeUiState,
    onRecipeClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onAddRecipeClick: () -> Unit
) {
    val filteredRecipes = uiState.recetas.filter {
        it.nombre.contains(uiState.searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopBar(title = "Mis recetas") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipeClick,
                containerColor = Color(0xFF006C47),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Nueva Receta")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Filled.Home, null) }, label = { Text("Inicio") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.FavoriteBorder, null) }, label = { Text("Favoritos") })
                NavigationBarItem(selected = false, onClick = onAddRecipeClick, icon = { Icon(Icons.Filled.Add, null) }, label = { Text("Agregar") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.BarChart, null) }, label = { Text("Stats") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.Person, null) }, label = { Text("Perfil") })
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                LoadingView()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchChange,
                            label = { Text("Buscar receta...") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { /* Filtro */ }) {
                            Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filtro", tint = Color(0xFF006C47))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (filteredRecipes.isEmpty()) {
                        Text(text = "No hay recetas disponibles.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredRecipes) { recipe ->
                                RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockState = RecipeUiState(
        recetas = listOf(
            Recipe(1, "Pasta Alfredo", "Salsa cremosa", "Italiana",
                listOf("200g pasta", "100ml crema"),
                listOf("Cocinar pasta", "Agregar salsa"),
                30, 4.7, 5, emptyList()),
            Recipe(2, "Pollo al horno", "Con hierbas", "Carnes",
                listOf("1 pollo", "Especias"),
                listOf("Sazonar", "Hornear"),
                45, 4.2, 3, emptyList())
        ),
        searchQuery = ""
    )
    HomeScreen(
        uiState = mockState,
        onRecipeClick = {},
        onSearchChange = {},
        onAddRecipeClick = {}
    )
}