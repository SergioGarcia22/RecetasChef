package com.example.recetaschef.presentation.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recetaschef.presentation.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: RecipeViewModel,
    recipeId: Int,
    onNavigateBack: () -> Unit
) {
    val uiState     by viewModel.uiState.collectAsState()
    val reviewState by viewModel.reviewState.collectAsState()

    // Carga detalle y stats al entrar
    LaunchedEffect(recipeId) {
        viewModel.loadRecipeDetail(recipeId)
        viewModel.loadStats(recipeId)
    }

    // Mostrar snackbar si hay mensaje
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(reviewState.successMessage, reviewState.errorMessage) {
        reviewState.successMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
        reviewState.errorMessage?.let   { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.selectedRecipe?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val recipe = uiState.selectedRecipe ?: return@Scaffold
        val ingredientes = recipe.ingredientes
        val pasos        = recipe.pasos

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen
            item {
                if (recipe.fotoUrl.isNotBlank()) {
                    AsyncImage(
                        model = recipe.fotoUrl,
                        contentDescription = recipe.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Info general
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(recipe.nombre, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("📂 ${recipe.categoria}  •  🎯 ${recipe.dificultad}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text("⏱ ${recipe.tiempoPreparacion} min  •  🍽 ${recipe.porciones} porciones",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        if (recipe.descripcion.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(recipe.descripcion)
                        }
                    }
                }
            }

            // Stats
            uiState.stats?.let { stats ->
                item {
                    Card(shape = RoundedCornerShape(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatItem("⭐", String.format("%.1f", stats.puntuacionPromedio), "Puntuación")
                            StatItem("🔁", "${stats.vecesPreparada}", "Preparaciones")
                            StatItem("💬", "${stats.totalOpiniones}", "Opiniones")
                            StatItem("⏱", "${stats.tiempoPreparacion}m", "Tiempo")
                        }
                    }
                }
            }

            // Ingredientes
            if (ingredientes.isNotEmpty()) {
                item {
                    Text("Ingredientes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                items(ingredientes) { ing ->
                    Row(Modifier.fillMaxWidth()) {
                        Text("• ", color = MaterialTheme.colorScheme.primary)
                        Text("${ing.cantidad} ${ing.unidad} de ${ing.nombre}")
                    }
                }
            }

            // Pasos
            if (pasos.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Text("Preparación", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                items(pasos) { step ->
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "${step.numeroPaso}. ",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(step.descripcion)
                    }
                }
            }

            // Opiniones
            val opiniones = recipe.opiniones
            if (opiniones.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Text("Opiniones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                items(opiniones) { opinion ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    opinion.comensal.ifBlank { "Anónimo" },
                                    fontWeight = FontWeight.Bold
                                )
                                Row {
                                    (1..5).forEach { star ->
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (star <= opinion.puntuacion)
                                                Color(0xFFFFC107)
                                            else Color.LightGray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                            if (opinion.comentario.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    opinion.comentario,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            // Formulario opinión
            item {
                Spacer(Modifier.height(4.dp))
                Text("Dejar opinión", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                ReviewForm(
                    comensal     = reviewState.comensal,
                    comentario   = reviewState.comentario,
                    puntuacion   = reviewState.puntuacion,
                    porciones    = reviewState.porcionesUsados,
                    isLoading    = reviewState.isLoading,
                    onComensal   = viewModel::onReviewComensal,
                    onComentario = viewModel::onReviewComentario,
                    onPuntuacion = viewModel::onReviewPuntuacion,
                    onPorciones  = viewModel::onReviewPorciones,
                    onSubmit     = { viewModel.addReview(recipeId) }
                )
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun StatItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 20.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun ReviewForm(
    comensal: String,
    comentario: String,
    puntuacion: Float,
    porciones: Int,
    isLoading: Boolean,
    onComensal: (String) -> Unit,
    onComentario: (String) -> Unit,
    onPuntuacion: (Float) -> Unit,
    onPorciones: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

            OutlinedTextField(
                value = comensal,
                onValueChange = onComensal,
                label = { Text("Tu nombre (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = comentario,
                onValueChange = onComentario,
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            // Estrellas
            Text("Puntuación", fontWeight = FontWeight.Medium)
            Row {
                (1..5).forEach { star ->
                    IconButton(onClick = { onPuntuacion(star.toFloat()) }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "$star estrellas",
                            tint = if (star <= puntuacion) Color(0xFFFFC107)
                            else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Porciones
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Porciones: ", fontWeight = FontWeight.Medium)
                IconButton(onClick = { if (porciones > 1) onPorciones(porciones - 1) }) {
                    Text("−", fontSize = 20.sp)
                }
                Text("$porciones", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { onPorciones(porciones + 1) }) {
                    Text("+", fontSize = 20.sp)
                }
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(Modifier.size(20.dp))
                else Text("Guardar opinión")
            }
        }
    }
}