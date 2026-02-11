package com.dailyfocus.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * DailyFocus typography scale.
 * Uses the default system font family for maximum compatibility.
 * Weights and sizes are tuned for a calm, readable experience.
 */
val DailyFocusTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp // Tighter for large headings
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp // Slight breathing room
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp // Optimized for reading
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp // Caps usually need more spacing
    )
)

/**
 * Returns a Typography instance with all font sizes and line heights multiplied by [scale].
 */
fun getScaledTypography(scale: Float): Typography {
    if (scale == 1.0f) return DailyFocusTypography
    
    return Typography(
        displayLarge = DailyFocusTypography.displayLarge.copy(
            fontSize = DailyFocusTypography.displayLarge.fontSize * scale,
            lineHeight = DailyFocusTypography.displayLarge.lineHeight * scale
        ),
        headlineLarge = DailyFocusTypography.headlineLarge.copy(
            fontSize = DailyFocusTypography.headlineLarge.fontSize * scale,
            lineHeight = DailyFocusTypography.headlineLarge.lineHeight * scale
        ),
        headlineMedium = DailyFocusTypography.headlineMedium.copy(
            fontSize = DailyFocusTypography.headlineMedium.fontSize * scale,
            lineHeight = DailyFocusTypography.headlineMedium.lineHeight * scale
        ),
        titleLarge = DailyFocusTypography.titleLarge.copy(
            fontSize = DailyFocusTypography.titleLarge.fontSize * scale,
            lineHeight = DailyFocusTypography.titleLarge.lineHeight * scale
        ),
        titleMedium = DailyFocusTypography.titleMedium.copy(
            fontSize = DailyFocusTypography.titleMedium.fontSize * scale,
            lineHeight = DailyFocusTypography.titleMedium.lineHeight * scale
        ),
        bodyLarge = DailyFocusTypography.bodyLarge.copy(
            fontSize = DailyFocusTypography.bodyLarge.fontSize * scale,
            lineHeight = DailyFocusTypography.bodyLarge.lineHeight * scale
        ),
        bodyMedium = DailyFocusTypography.bodyMedium.copy(
            fontSize = DailyFocusTypography.bodyMedium.fontSize * scale,
            lineHeight = DailyFocusTypography.bodyMedium.lineHeight * scale
        ),
        bodySmall = DailyFocusTypography.bodySmall.copy(
            fontSize = DailyFocusTypography.bodySmall.fontSize * scale,
            lineHeight = DailyFocusTypography.bodySmall.lineHeight * scale
        ),
        labelLarge = DailyFocusTypography.labelLarge.copy(
            fontSize = DailyFocusTypography.labelLarge.fontSize * scale,
            lineHeight = DailyFocusTypography.labelLarge.lineHeight * scale
        ),
        labelMedium = DailyFocusTypography.labelMedium.copy(
            fontSize = DailyFocusTypography.labelMedium.fontSize * scale,
            lineHeight = DailyFocusTypography.labelMedium.lineHeight * scale
        ),
        labelSmall = DailyFocusTypography.labelSmall.copy(
            fontSize = DailyFocusTypography.labelSmall.fontSize * scale,
            lineHeight = DailyFocusTypography.labelSmall.lineHeight * scale
        )
    )
}
