package com.example.contexta.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun CircularRevealLayout(
    visible: Boolean,
    revealFrom: Offset,
    modifier: Modifier = Modifier,
    durationMillis: Int = 450,
    content: @Composable () -> Unit
) {
    val animatedFraction by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        label = "circularReveal"
    )

    if (animatedFraction > 0f) {
        Box(
            modifier = modifier
                .graphicsLayer { }
                .clip(CircularRevealShape(animatedFraction, revealFrom))
        ) {
            content()
        }
    }
}

private class CircularRevealShape(
    private val fraction: Float,
    private val origin: Offset
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val maxRadius = sqrt(
            max(origin.x, size.width - origin.x).pow(2) +
            max(origin.y, size.height - origin.y).pow(2)
        )
        val path = Path().apply {
            addOval(Rect(center = origin, radius = maxRadius * fraction))
        }
        return Outline.Generic(path)
    }
}
