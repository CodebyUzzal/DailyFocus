package com.dailyfocus.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.dailyfocus.core.ui.theme.Spacing

/**
 * Clean, text-focused empty state with optional icon.
 * Avoids heavy illustrations for a "calm" look.
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    subMessage: String? = null,
    icon: ImageVector? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(Spacing.xl)
            )
            Spacer(modifier = Modifier.height(Spacing.m))
        }
        
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        if (subMessage != null) {
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = subMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
