package com.example.contexta.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

// 1. Data Structure
data class GoogleButtonColors(
    val fill: Color,
    val stroke: Color,
    val font: Color
)

// 2. The Values (keeping them private to the file is cleaner)
private val GoogleLight = GoogleButtonColors(
    fill = GoogleLightFill,
    stroke = GoogleLightStroke,
    font = GoogleLightFont
)

private val GoogleDark = GoogleButtonColors(
    fill = GoogleDarkFill,
    stroke = GoogleDarkStroke,
    font = GoogleDarkFont
)

// 3. The CompositionLocal
val LocalGoogleColors = staticCompositionLocalOf { GoogleLight }

// 4. The Extension Property
val MaterialTheme.googleColors: GoogleButtonColors
    @Composable
    @ReadOnlyComposable
    get() = LocalGoogleColors.current

// 5. Helper function to pick the right palette
fun getGoogleColors(isDark: Boolean): GoogleButtonColors {
    return if (isDark) GoogleDark else GoogleLight
}