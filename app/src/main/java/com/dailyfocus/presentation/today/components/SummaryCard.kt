package com.dailyfocus.presentation.today.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumProgressBar
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.GradientSummaryEnd
import com.dailyfocus.core.ui.theme.GradientSummaryStart
import com.dailyfocus.core.ui.theme.Spacing

@Composable
fun SummaryCard(
    completionPercentage: Float,
    modifier: Modifier = Modifier
) {
    DailyFocusCard(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.ExtraLarge,
        elevation = Elevation.Level2
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(GradientSummaryStart, GradientSummaryEnd)
                    )
                )
                .padding(Spacing.l)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Progress",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = "${(completionPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(Spacing.m))
                
                PremiumProgressBar(
                    progress = completionPercentage,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f),
                    height = 8.dp
                )
            }
        }
    }
}
