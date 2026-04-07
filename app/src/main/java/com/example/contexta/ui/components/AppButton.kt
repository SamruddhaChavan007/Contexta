package com.example.contexta.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contexta.ui.theme.CyanBlue
import com.example.contexta.ui.theme.MagentaPurple
import com.example.contexta.ui.theme.RoyalBlue
import com.example.contexta.ui.theme.manropeFontFamily

@Composable
fun AppButton(
    text: String,
    height: Dp,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    var widthPx by remember { mutableFloatStateOf(0f) }

    val transition = rememberInfiniteTransition(label = "shimmer")

    val shimmerX by transition.animateFloat(
        initialValue = -2 * widthPx,
        targetValue = 2 * widthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerX"
    )

    val alpha = if (enabled) 1f else 0.4f

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    widthPx = it.size.width.toFloat()
                }
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            CyanBlue.copy(alpha = alpha),
                            RoyalBlue.copy(alpha = alpha),
                            MagentaPurple.copy(alpha = alpha)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(widthPx, 0f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxSize()
        ) {
            if (enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.linearGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Transparent,
                                    0.4f to Color.Transparent,

                                    // shimmer peak (only in blue)
                                    0.5f to Color.White.copy(alpha = 0.08f),

                                    // fade OUT before cyan
                                    0.58f to Color.White.copy(alpha = 0.01f),
                                    0.62f to Color.Transparent,

                                    // completely gone in cyan
                                    1.0f to Color.Transparent
                                ),
                                start = Offset(shimmerX - widthPx, 0f),
                                end = Offset(shimmerX, 0f)
                            )
                        )
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = Color.White.copy(alpha = alpha),
                    fontFamily = manropeFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_AppButton() {
    AppButton(
        text = "Create Account",
        height = 50.dp,
        enabled = true,
        onClick = { /*N/A*/ }
    )
}