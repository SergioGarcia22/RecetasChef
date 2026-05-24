package com.example.recetaschef.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Float,
    maxStars: Int = 5,
    onRatingChange: ((Int) -> Unit)? = null
) {
    Row {
        for (i in 1..maxStars) {
            val isSelected = i <= kotlin.math.round(rating).toInt()
            Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Estrella $i",
                tint = Color(0xFFFBC02D),
                modifier = Modifier
                    .size(22.dp)
                    .clickable(enabled = onRatingChange != null) {
                        onRatingChange?.invoke(i)
                    }
            )
        }
    }
}