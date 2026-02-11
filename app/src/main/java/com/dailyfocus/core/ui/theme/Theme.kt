package com.dailyfocus.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,
    primaryContainer = Primary80,
    onPrimaryContainer = Primary30,
    secondary = Secondary40,
    onSecondary = Color.White,
    secondaryContainer = Secondary80,
    onSecondaryContainer = Secondary40,
    tertiary = Tertiary40,
    onTertiary = Color.White,
    tertiaryContainer = Tertiary80,
    onTertiaryContainer = Tertiary40,
    error = Error40,
    onError = Color.White,
    errorContainer = Error80,
    onErrorContainer = Error40,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant30
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary30,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary80,
    secondary = Secondary80,
    onSecondary = Secondary40,
    secondaryContainer = Secondary40,
    onSecondaryContainer = Secondary80,
    tertiary = Tertiary80,
    onTertiary = Tertiary40,
    tertiaryContainer = Tertiary40,
    onTertiaryContainer = Tertiary80,
    error = Error80,
    onError = Error40,
    errorContainer = Error40,
    onErrorContainer = Error80,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = Neutral90, // Lighter text on dark variant
    outline = Neutral90
)

/**
 * DailyFocus Material 3 theme with automatic light/dark switching.
 * Sets the status bar color to match the surface for a seamless look.
 */
@Composable
fun DailyFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()

            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DailyFocusTypography,
        content = content
    )
}
