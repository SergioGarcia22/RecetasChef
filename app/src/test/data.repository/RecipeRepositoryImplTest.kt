package com.example.recetaschef.data.repository

import com.example.recetaschef.data.model.RecipeDto
import com.example.recetaschef.data.model.RecipeStatsDto
import com.example.recetaschef.data.model.RecipeDetailDto
import com.example.recetaschef.data.model.CreateRecipeRequest
import com.example.recetaschef.data.model.CreateReviewRequest
import com.example.recetaschef.data.remote.RecipeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Prueba unitaria del RecipeRepositoryImpl.
 * Se usa una implementación falsa (Fake) de RecipeApiService
 * para simular respuestas del backend sin necesidad de red.
 */
class RecipeRepositoryImplTest {

    private lateinit var fakeApiService: RecipeApiService
    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        // Fake API que retorna datos de prueba controlados
        fakeApiService = object : RecipeApiService {

            override suspend fun getRecipes(): List<RecipeDto> = listOf(
                RecipeDto(1, "Ajiaco", "Sopas", 60, null, 4.5),
                RecipeDto(2, "Bandeja Paisa", "Platos fuertes", 90, null, 4.8)
            )

            override suspend fun getRecipeDetail(id: Int) = RecipeDetailDto(
                id = 1,
                nombre = "Ajiaco",
                categoria = "Sopas",
                tiempoPreparacion = 60,
                imagen = null,
                ingredientes = listOf("Papa criolla", "Pollo", "Mazorca"),
                pasos = listOf("Hervir el pollo", "Agregar las papas"),
                puntuacionPromedio = 4.5,
                vecesPreparada = 10,
                opiniones = emptyList()
            )

            override suspend fun getRecipeStats(id: Int) = RecipeStatsDto(
                recetaId = 1,
                puntuacionPromedio = 4.5,
                vecesPreparada = 10,
                totalOpiniones = 3
            )

            override suspend fun postRecipe(body: CreateRecipeRequest) { /* no-op */ }

            override suspend fun postReview(recetaId: Int, body: CreateReviewRequest) { /* no-op */ }
        }

        repository = RecipeRepositoryImpl(fakeApiService)
    }

    @Test
    fun `getRecipes retorna la lista correcta de recetas`() = runBlocking {
        val recipes = repository.getRecipes()
        assertEquals(2, recipes.size)
        assertEquals("Ajiaco", recipes[0].nombre)
        assertEquals("Bandeja Paisa", recipes[1].nombre)
    }

    @Test
    fun `getRecipes mapea correctamente la puntuacion promedio`() = runBlocking {
        val recipes = repository.getRecipes()
        assertEquals(4.5, recipes[0].puntuacionPromedio, 0.01)
    }

    @Test
    fun `getRecipeDetail retorna el detalle correcto`() = runBlocking {
        val detail = repository.getRecipeDetail(1)
        assertEquals(1, detail.id)
        assertEquals("Ajiaco", detail.nombre)
        assertEquals(3, detail.ingredientes.size)
        assertEquals(10, detail.vecesPreparada)
    }

    @Test
    fun `getRecipeStats retorna estadisticas correctas`() = runBlocking {
        val stats = repository.getRecipeStats(1)
        assertEquals(1, stats.recetaId)
        assertEquals(4.5, stats.puntuacionPromedio, 0.01)
        assertEquals(10, stats.vecesPreparada)
        assertEquals(3, stats.totalOpiniones)
    }

    @Test
    fun `puntuacion promedio calculada correctamente para lista de recetas`() = runBlocking {
        // Valida que el promedio de puntuaciones de la lista sea el esperado
        val recipes = repository.getRecipes()
        val promedio = recipes.map { it.puntuacionPromedio }.average()
        assertEquals(4.65, promedio, 0.01)
    }
}
