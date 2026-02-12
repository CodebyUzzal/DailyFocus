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
    secondary = Secondary40,
    onSecondary = Color.White,
    tertiary = Tertiary40,
    onTertiary = Color.White,
    background = LightSurface1,
    surface = LightSurface2, // Cards
    onSurface = Neutral10,
    surfaceVariant = LightSurface3,
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary30,
    secondary = Secondary80,
    onSecondary = Secondary40,
    tertiary = Tertiary80,
    onTertiary = Tertiary40,
    background = Surface1, // Deepest (App Background)
    surface = Surface3,    // Cards
    onSurface = Neutral90,
    surfaceVariant = Surface4, // Elevated
    onSurfaceVariant = Neutral90,
    outline = Color(0xFF44474F)
)

/**
 * DailyFocus Material 3 theme with automatic light/dark switching.
 * Sets the status bar color to match the surface for a seamless look.
 */
@Composable
fun DailyFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScale: Float = 1.0f,
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
        typography = getScaledTypography(fontScale),
        content = content
    )
}
