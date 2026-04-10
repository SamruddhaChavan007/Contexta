package com.example.contexta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contexta.data.theme.ThemePreference
import com.example.contexta.ui.theme.ContextaTheme

private fun ThemePreference.icon(): ImageVector = when (this) {
    ThemePreference.LIGHT  -> Icons.Default.WbSunny
    ThemePreference.DARK   -> Icons.Default.DarkMode
    ThemePreference.SYSTEM -> Icons.Default.Monitor
}

@Composable
fun ThemeChangeRow(
    selected: ThemePreference,
    onSelect: (ThemePreference) -> Unit
) {
    val options = ThemePreference.entries

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option == selected

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (index == 0) 0.dp else 4.dp,
                        end = if (index == options.lastIndex) 0.dp else 4.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent
                    )
                    .clickable { onSelect(option) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = option.icon(),
                    contentDescription = option.name,
                    tint = if (isSelected) Color(0xFFFF5722) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Preview_ThemeChangeRow() {
    ContextaTheme(dynamicColor = false) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            ThemeChangeRow(
                selected = ThemePreference.SYSTEM,
                onSelect = {}
            )
        }
    }
}
