package com.dailyfocus.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * DailyFocus typography scale.
 * 
 * Hierarchy:
 * - Display: Greeting / Large counters
 * - Headline: Section Headers
 * - Title: Task Titles / Card Headers
 * - Body: Primary Content / Descriptions
 * - Label: Buttons / Tags / Metadata
 */
val DailyFocusTypography = Typography(
    // Greeting only
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-1.0).sp // Tight & Modern
    ),
    // Section Headers ("Today", "Habits")
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    // Task Titles
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp, // Apple-like standard body size
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Standard Body text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    // Secondary text / subtitles
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    // Captions / Dates
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp,
        color = Neutral90.copy(alpha = 0.7f) // Built-in opacity hint
    ),
    // Buttons / Chips
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)

fun getScaledTypography(scale: Float): Typography {
    if (scale == 1.0f) return DailyFocusTypography

    val constrainedScale = scale.coerceIn(0.85f, 1.30f)

    return Typography(
        displayLarge = DailyFocusTypography.displayLarge.scale(constrainedScale),
        displayMedium = DailyFocusTypography.displayMedium.scale(constrainedScale),
        displaySmall = DailyFocusTypography.displaySmall.scale(constrainedScale),
        headlineLarge = DailyFocusTypography.headlineLarge.scale(constrainedScale),
        headlineMedium = DailyFocusTypography.headlineMedium.scale(constrainedScale),
        headlineSmall = DailyFocusTypography.headlineSmall.scale(constrainedScale),
        titleLarge = DailyFocusTypography.titleLarge.scale(constrainedScale),
        titleMedium = DailyFocusTypography.titleMedium.scale(constrainedScale),
        titleSmall = DailyFocusTypography.titleSmall.scale(constrainedScale),
        bodyLarge = DailyFocusTypography.bodyLarge.scale(constrainedScale),
        bodyMedium = DailyFocusTypography.bodyMedium.scale(constrainedScale),
        bodySmall = DailyFocusTypography.bodySmall.scale(constrainedScale),
        labelLarge = DailyFocusTypography.labelLarge.scale(constrainedScale),
        labelMedium = DailyFocusTypography.labelMedium.scale(constrainedScale),
        labelSmall = DailyFocusTypography.labelSmall.scale(constrainedScale)
    )
}

private fun TextStyle.scale(scale: Float): TextStyle {
    return this.copy(
        fontSize = this.fontSize * scale,
        lineHeight = this.lineHeight * scale,
        letterSpacing = this.letterSpacing * scale
    )
}
