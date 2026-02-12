package com.dailyfocus.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.AnimationConstants
import com.dailyfocus.core.ui.theme.GradientHeaderEnd
import com.dailyfocus.core.ui.theme.GradientHeaderStart
import com.dailyfocus.core.ui.theme.Spacing

@Composable
fun CompactGreetingHeader(
    username: String,
    date: String,
    progress: Float,
    taskCount: Int,
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }

    // Time-based greeting
    val greeting = remember {
        val hour = java.time.LocalTime.now().hour
        when {
            hour < 12 -> "Good Morning,"
            hour < 17 -> "Good Afternoon,"
            else -> "Good Evening,"
        }
    }

    val remainingCount = taskCount - completedCount

    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(animationSpec = AnimationConstants.normalTween()),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.m, vertical = Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            // Row 1: Greeting + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = username,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 4.dp) // Align with baseline
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.xs))

            // Row 2: Progress Bar
            PremiumProgressBar(
                progress = progress,
                height = 6.dp,
                modifier = Modifier.fillMaxWidth()
            )

            // Row 3: Summary text
            Text(
                text = "$taskCount Tasks • $completedCount Completed • $remainingCount Remaining",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(Spacing.m))
        Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}
