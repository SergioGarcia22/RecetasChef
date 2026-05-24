package com.example.recetaschef.presentation.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recetaschef.domain.model.Recipe
import com.example.recetaschef.presentation.components.LoadingView
import com.example.recetaschef.presentation.components.RatingBar
import com.example.recetaschef.presentation.state.RecipeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: RecipeUiState,
    onNavigateToStats: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val recipe = uiState.recetaDetalle

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.nombre ?: "Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "Favorito") }
                    IconButton(onClick = {}) { Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir") }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading || recipe == null) {
                LoadingView()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500",
                        contentDescription = recipe.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SuggestionChip(onClick = {}, label = { Text(recipe.categoria) })
                        SuggestionChip(onClick = {}, label = { Text("${recipe.tiempoPreparacion} Min") })
                        SuggestionChip(onClick = {}, label = { Text("Fácil") })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        RatingBar(rating = recipe.puntuacionPromedio)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${recipe.puntuacionPromedio} (${recipe.vecesPreparada} preparaciones)", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = recipe.descripcion ?: "", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Ingredientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    recipe.ingredientes.forEach { ingrediente ->
                        Text(text = "• $ingrediente", modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Preparación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    recipe.preparacion.forEachIndexed { index, paso ->
                        Text(text = "${index + 1}. $paso", modifier = Modifier.padding(start = 8.dp, top = 6.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { if (recipe.id > 0) onNavigateToStats(recipe.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006C47))
                    ) {
                        Text(text = "Ver estadísticas", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val mockRecipe = Recipe(
        1, "Pasta Alfredo", "Clásica salsa cremosa italiana.", "Italiana",
        listOf("200g pasta fettuccine", "100ml crema de leche", "50g queso parmesano"),
        listOf("Cocinar la pasta", "En un sartón, derretir mantequilla", "Agregar la crema"),
        30, 4.7, 5, emptyList()
    )
    DetailScreen(
        uiState = RecipeUiState(recetaDetalle = mockRecipe),
        onNavigateToStats = {},
        onBackClick = {}
    )
}