package com.example.slidepuzzle.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BlurredBeamsBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "beams")
    
    val color1 = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val color2 = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
    val color3 = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)

    val animValue1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beam1"
    )

    val animValue2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beam2"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .blur(60.dp)
    ) {
        val width = size.width
        val height = size.height

        // Beam 1
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(width * animValue1, height * (1 - animValue2)),
                radius = width * 0.8f
            ),
            center = Offset(width * animValue1, height * (1 - animValue2)),
            radius = width * 0.8f
        )

        // Beam 2
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(width * (1 - animValue1), height * animValue2),
                radius = width * 0.7f
            ),
            center = Offset(width * (1 - animValue1), height * animValue2),
            radius = width * 0.7f
        )

        // Beam 3
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color3, Color.Transparent),
                center = Offset(width * 0.5f, height * animValue1),
                radius = width * 0.9f
            ),
            center = Offset(width * 0.5f, height * animValue1),
            radius = width * 0.9f
        )
    }
}
