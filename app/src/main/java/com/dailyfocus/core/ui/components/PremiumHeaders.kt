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
fun GreetingHeader(
    username: String = "Uzzal",
    date: String,
    modifier: Modifier = Modifier
) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(animationSpec = AnimationConstants.normalTween())
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.m, vertical = Spacing.l)
        ) {
            Text(
                text = "Good Evening,",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = username,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
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
