package com.dailyfocus.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.AnimationConstants

@Composable
fun PremiumCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = AnimationConstants.normalTween(),
        label = "circularProgress"
    )

    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val diameter = size.toPx()
        val radius = diameter / 2f
        val topLeftOffset = Offset(
            (size.toPx() - diameter) / 2, 
            (size.toPx() - diameter) / 2
        ) // Actually 0,0 if size matches canvas size

        // Track
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke,
            size = Size(diameter - strokeWidth.toPx(), diameter - strokeWidth.toPx()),
            topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2)
        )

        // Progress
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = stroke,
            size = Size(diameter - strokeWidth.toPx(), diameter - strokeWidth.toPx()),
            topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2)
        )
    }
}
