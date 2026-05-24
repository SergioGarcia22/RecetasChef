package com.example.recetaschef.domain.model

import com.google.gson.annotations.SerializedName

data class Stats(
    val nombre: String = "",
    @SerializedName("puntuacion_promedio")
    val puntuacionPromedio: Float = 0f,
    @SerializedName("total_opiniones")
    val totalOpiniones: Int = 0,
    @SerializedName("veces_preparada")
    val vecesPreparada: Int = 0,
    @SerializedName("porciones_promedio")
    val porcionesPromedio: Float = 0f,
    @SerializedName("tiempo_preparacion")
    val tiempoPreparacion: Int = 0,
    val historial: List<HistorialItem> = emptyList()
)

data class HistorialItem(
    val fecha: String = "",
    val porciones: Int = 0
)