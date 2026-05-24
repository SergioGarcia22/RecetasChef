package com.example.recetaschef.presentation.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetaschef.domain.model.request.IngredienteRequest
import com.example.recetaschef.domain.model.request.PasoRequest
import com.example.recetaschef.presentation.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var nombre      by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria   by remember { mutableStateOf("") }
    var tiempo      by remember { mutableStateOf("") }
    var porciones   by remember { mutableStateOf("2") }
    var dificultad  by remember { mutableStateOf("Fácil") }
    var fotoUrl     by remember { mutableStateOf("") }

    // Listas temporales para mostrar en UI
    val ingredientes = remember { mutableStateListOf<IngredienteRequest>() }
    val pasos        = remember { mutableStateListOf<PasoRequest>() }

    var ingNombre   by remember { mutableStateOf("") }
    var ingCantidad by remember { mutableStateOf("") }
    var ingUnidad   by remember { mutableStateOf("") }
    var pasoDesc    by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
            onNavigateBack()
        }
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    val dificultades = listOf("Fácil", "Media", "Difícil")
    var expandedDificultad by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Nueva receta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Datos generales ───────────────────────────
            item { Text("Datos generales", fontWeight = FontWeight.Bold, fontSize = 18.sp) }

            item {
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre de la receta *") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = descripcion, onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(), minLines = 2
                )
            }
            item {
                OutlinedTextField(
                    value = categoria, onValueChange = { categoria = it },
                    label = { Text("Categoría (ej: Italiana)") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tiempo, onValueChange = { tiempo = it },
                        label = { Text("Tiempo (min)") },
                        modifier = Modifier.weight(1f), singleLine = true
                    )
                    OutlinedTextField(
                        value = porciones, onValueChange = { porciones = it },
                        label = { Text("Porciones") },
                        modifier = Modifier.weight(1f), singleLine = true
                    )
                }
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedDificultad,
                    onExpandedChange = { expandedDificultad = it }
                ) {
                    OutlinedTextField(
                        value = dificultad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dificultad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDificultad) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDificultad,
                        onDismissRequest = { expandedDificultad = false }
                    ) {
                        dificultades.forEach { op ->
                            DropdownMenuItem(
                                text = { Text(op) },
                                onClick = { dificultad = op; expandedDificultad = false }
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = fotoUrl, onValueChange = { fotoUrl = it },
                    label = { Text("URL de foto (opcional)") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
            }

            // ── Ingredientes ──────────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Text("Ingredientes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            items(ingredientes.size) { i ->
                val ing = ingredientes[i]
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("• ${ing.cantidad} ${ing.unidad} de ${ing.nombre}",
                        modifier = Modifier.weight(1f))
                    IconButton(onClick = { ingredientes.removeAt(i) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            item {
                Card(shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ingCantidad, onValueChange = { ingCantidad = it },
                                label = { Text("Cantidad") },
                                modifier = Modifier.weight(1f), singleLine = true
                            )
                            OutlinedTextField(
                                value = ingUnidad, onValueChange = { ingUnidad = it },
                                label = { Text("Unidad") },
                                modifier = Modifier.weight(1f), singleLine = true
                            )
                        }
                        OutlinedTextField(
                            value = ingNombre, onValueChange = { ingNombre = it },
                            label = { Text("Nombre ingrediente") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        OutlinedButton(
                            onClick = {
                                if (ingNombre.isNotBlank()) {
                                    ingredientes.add(
                                        IngredienteRequest(
                                            nombre   = ingNombre,
                                            cantidad = ingCantidad,
                                            unidad   = ingUnidad
                                        )
                                    )
                                    ingNombre = ""; ingCantidad = ""; ingUnidad = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("+ Agregar ingrediente") }
                    }
                }
            }

            // ── Pasos ─────────────────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Text("Preparación", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            items(pasos.size) { i ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${i + 1}. ${pasos[i].descripcion}",
                        modifier = Modifier.weight(1f))
                    IconButton(onClick = { pasos.removeAt(i) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            item {
                Card(shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = pasoDesc, onValueChange = { pasoDesc = it },
                            label = { Text("Descripción del paso") },
                            modifier = Modifier.fillMaxWidth(), minLines = 2
                        )
                        OutlinedButton(
                            onClick = {
                                if (pasoDesc.isNotBlank()) {
                                    pasos.add(PasoRequest(descripcion = pasoDesc))
                                    pasoDesc = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("+ Agregar paso") }
                    }
                }
            }

            // ── Botón guardar ─────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nombre.isBlank()) return@Button
                        viewModel.saveRecipe(
                            nombre            = nombre,
                            descripcion       = descripcion,
                            categoria         = categoria,
                            tiempoPreparacion = tiempo.toIntOrNull() ?: 0,
                            porciones         = porciones.toIntOrNull() ?: 2,
                            dificultad        = dificultad,
                            fotoUrl           = fotoUrl,
                            ingredientes      = ingredientes.toList(),
                            pasos             = pasos.toList()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = nombre.isNotBlank() && !uiState.isLoading
                ) {
                    if (uiState.isLoading) CircularProgressIndicator(Modifier.size(20.dp))
                    else Text("Guardar receta", fontSize = 16.sp)
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}