package com.dailyfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.ui.theme.DailyFocusTheme
import com.dailyfocus.presentation.navigation.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Single Activity entry point for DailyFocus.
 * All navigation is handled within Compose via Navigation Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val fontScale by appPreferences.fontScale.collectAsState(initial = 1.0f)
            val themeMode by appPreferences.themeMode.collectAsState(initial = AppPreferences.THEME_SYSTEM)
            
            val darkTheme = when (themeMode) {
                AppPreferences.THEME_LIGHT -> false
                AppPreferences.THEME_DARK -> true
                else -> isSystemInDarkTheme()
            }

            DailyFocusTheme(
                darkTheme = darkTheme,
                fontScale = fontScale
            ) {
                MainScreen(appPreferences = appPreferences)
            }
        }
    }
}
