package com.dailyfocus.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.AnimationConstants

@Composable
fun PremiumProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = AnimationConstants.normalTween(),
        label = "progressAnimation"
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(height / 2)),
        color = color,
        trackColor = trackColor,
    )
}
