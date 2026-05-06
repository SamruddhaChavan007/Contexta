package com.example.contexta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contexta.ui.theme.ContextaTheme
import com.example.contexta.ui.theme.manropeFontFamily

@Composable
fun CircleInitialHolder(
    username: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    ) {
        Text(
            text = getInitials(username),
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 34.sp
        )
    }
}

fun getInitials(name: String): String {
    if (name.isBlank()) return "CU"

    return name
        .trim()
        .split(Regex("\\s+"))
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
        .take(2)
}

@Preview
@Composable
fun Preview_CircleInitialHolder() {
    ContextaTheme(
        dynamicColor = false
    ) {
        CircleInitialHolder("SC")
    }
}