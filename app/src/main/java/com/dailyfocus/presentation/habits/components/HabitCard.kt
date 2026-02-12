package com.dailyfocus.presentation.habits.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumCheckbox
import com.dailyfocus.core.ui.theme.*
import com.dailyfocus.domain.usecase.habits.HabitWithStreak

@Composable
fun HabitCard(
    habitWithStreak: HabitWithStreak,
    onToggleToday: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DailyFocusCard(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // For vertical stripe to fill height
        onClick = onClick,
        elevation = Elevation.Level1
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Accent Stripe
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Spacing.xs)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Tertiary80, Tertiary40)
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Info Section
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habitWithStreak.habit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    
                    // Streak & Stats
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Subtle flame animation if streak > 0
                        val isStreakActive = habitWithStreak.currentStreak > 0
                        
                        /* 
                         * User Feedback: "No infinite looping flame animation... Trigger only when streak updates [or] Be subtle (alpha pulse)"
                         * Since we don't know "when streak updates" easily in this statutory view without extra state,
                         * we will do a VERY subtle alpha pulse if streak > 0, almost static.
                         */
                         
                        val infiniteTransition = rememberInfiniteTransition(label = "flamePulse")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.7f,
                            targetValue = if (isStreakActive) 1.0f else 0.7f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000), // Very slow pulse
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "flameAlpha"
                        )

                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = if (isStreakActive) StreakHot else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(16.dp)
                                .alpha(if (isStreakActive) alpha else 0.5f)
                        )
                        Spacer(modifier = Modifier.width(Spacing.xxs))
                        Text(
                            text = "${habitWithStreak.currentStreak} day streak",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isStreakActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(Spacing.s))

                    // Weekly History Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        habitWithStreak.last7Days.forEach { completed ->
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.width(Spacing.m))

                // Toggle
                PremiumCheckbox(
                    checked = habitWithStreak.completedToday,
                    onCheckedChange = { onToggleToday() }
                )
            }
        }
    }
}
