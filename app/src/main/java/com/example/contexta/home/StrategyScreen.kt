package com.example.contexta.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contexta.R
import com.example.contexta.ui.components.StrategyCard
import com.example.contexta.ui.theme.ContextaTheme
import com.example.contexta.ui.theme.caveatFontFamily
import com.example.contexta.ui.theme.manropeFontFamily

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StrategyScreen(
    viewModel: StrategyScreenViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(18.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Text(
                    text = "Contexta is ready",
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 12.sp,
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }


            Text(
                text = "Tap to speak",
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = manropeFontFamily,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "${uiState.dayName}, ${uiState.currentTime} · ${uiState.greeting}",
            color = Color.Gray,
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = "3 things need\nyour focus today.",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 32.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 40.sp,
            fontFamily = caveatFontFamily,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = "2 pending from yesterday · 1 new priority",
            color = Color.Gray,
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        StrategyCard(modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        Text(
            text = "QUICK ACTIONS",
            fontFamily = manropeFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        val itemsList = (0..3).toList() // 4 items

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf("Voice", "Note", "Task", "Queue")

            items.forEach { label ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { /* ToDo */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.mic),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.LightGray)
                            )
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewStrategyScreen() {
    ContextaTheme(
        dynamicColor = false
    ) {
        StrategyScreen()
    }
}