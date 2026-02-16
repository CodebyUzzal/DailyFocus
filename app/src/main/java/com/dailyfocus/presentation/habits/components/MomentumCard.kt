package com.dailyfocus.presentation.habits.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.MomentumGradientEnd
import com.dailyfocus.core.ui.theme.MomentumGradientStart
import com.dailyfocus.core.ui.theme.Radius
import com.dailyfocus.core.ui.theme.Spacing

/**
 * Premium Momentum Card (Phase 1).
 * Replaces the standard streak view with a motivation-first design.
 */
@Composable
fun MomentumCard(
    currentStreak: Int,
    longestStreak: Int,
    modifier: Modifier = Modifier
) {
    // Detect streak increase to trigger animation
    var previousStreak by remember { mutableIntStateOf(currentStreak) }
    val isStreakIncreased = currentStreak > previousStreak
    
    // Update previous streak after composition
    SideEffect {
        previousStreak = currentStreak
    }

    val scale by animateFloatAsState(
        targetValue = if (isStreakIncreased) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    // Flame pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "flamePulse")
    val flameGlow by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameGlow"
    )

    // Premium deep gradient
    val gradient = Brush.linearGradient(
        colors = listOf(
            MomentumGradientStart,
            MomentumGradientEnd,
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Streak & Motivation
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Animated Flame
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Flame",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(56.dp)
                                .scale(flameGlow)
                        )
                        
                        // Streak Count
                        Column {
                            Text(
                                text = "$currentStreak Day Streak",
                                style = MaterialTheme.typography.displayMedium, // 48-56sp equivalent
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (currentStreak > 0) "You're building momentum" else "Start your streak today",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Right: Best Stat
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Best",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$longestStreak days",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface 
                    )
                }
            }
        }
    }
}
