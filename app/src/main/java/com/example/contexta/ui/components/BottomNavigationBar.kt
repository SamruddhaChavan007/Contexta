package com.example.contexta.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.contexta.navigation.model.navigationItems
import com.example.contexta.ui.theme.ContextaTheme
import com.example.contexta.ui.theme.ExecutiveBlue


@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = navigationItems.indexOfFirst {
        it.route == currentRoute
    }.coerceAtLeast(0)

    var barSize by remember { mutableStateOf(IntSize(0, 0)) }

    val offsetStep = remember(barSize) {
        if (barSize.width == 0) 0f else barSize.width.toFloat() / (navigationItems.size * 2)
    }

    val targetOffset = remember(selectedIndex, offsetStep) {
        if (offsetStep == 0f) 0f else offsetStep + selectedIndex * 2 * offsetStep
    }

    val circleRadius = 26.dp
    val circlePx = with(LocalDensity.current) { circleRadius.toPx().toInt() }

    val transition = updateTransition(targetState = targetOffset, label = "cutout-offset")

    val springSpec = spring<Float>(
        dampingRatio = 0.5f,
        stiffness = Spring.StiffnessVeryLow
    )

    val cutoutOffset by transition.animateFloat(
        transitionSpec = { if (initialState == 0f) snap() else springSpec },
        label = "cutout"
    ) { it }

    val circleOffset by transition.animateIntOffset(
        transitionSpec = {
            if (initialState == 0f) snap()
            else spring(springSpec.dampingRatio, springSpec.stiffness)
        },
        label = "circle"
    ) {
        IntOffset(it.toInt() - circlePx, -circlePx)
    }

    val barShape = remember(cutoutOffset) {
        BarShape(
            offset = cutoutOffset,
            circleRadius = circleRadius,
            cornerRadius = 25.dp
        )
    }

    Box {

        Box(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f)
                .size(circleRadius * 2)
                .clip(CircleShape)
                .background(ExecutiveBlue),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = navigationItems[selectedIndex].icon,
                label = "circle-icon"
            ) { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .onPlaced { barSize = it.size }
                .fillMaxWidth()
                .height(78.dp)
                .background(
                    Color.LightGray.copy(alpha = 0.3f),
                    shape = barShape
                )
                .animateContentSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            navigationItems.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                val interactionSource = remember { MutableInteractionSource() }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {},
                    icon = {
                        Box(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.alpha(if (isSelected) 0f else 1f)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

// ---------- Internal bits (shape) ----------

private class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2

        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 1.5f
            val cutoutLeftX = cutoutCenterX - cutoutEdgeOffset
            val cutoutRightX = cutoutCenterX + cutoutEdgeOffset

            // bottom left
            moveTo(0f, size.height)
            // top left corner (respect overlap with cutout)
            if (cutoutLeftX > 0) {
                val realLeftCornerDiameter =
                    if (cutoutLeftX >= cornerRadiusPx) cornerDiameter else cutoutLeftX * 2
                arcTo(
                    rect = Rect(0f, 0f, realLeftCornerDiameter, realLeftCornerDiameter),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            }
            lineTo(cutoutLeftX, 0f)
            // cutout
            cubicTo(
                cutoutCenterX - cutoutRadius, 0f,
                cutoutCenterX - cutoutRadius, cutoutRadius,
                cutoutCenterX, cutoutRadius
            )
            cubicTo(
                cutoutCenterX + cutoutRadius, cutoutRadius,
                cutoutCenterX + cutoutRadius, 0f,
                cutoutRightX, 0f
            )
            // top right corner
            if (cutoutRightX < size.width) {
                val realRightCornerDiameter =
                    if (cutoutRightX <= size.width - cornerRadiusPx) cornerDiameter
                    else (size.width - cutoutRightX) * 2
                arcTo(
                    rect = Rect(
                        size.width - realRightCornerDiameter, 0f,
                        size.width, realRightCornerDiameter
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            }
            // bottom right
            lineTo(size.width, size.height)
            close()
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    ContextaTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomNavigationBar(rememberNavController())
        }
    }
}