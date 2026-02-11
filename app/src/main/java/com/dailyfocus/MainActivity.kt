package com.dailyfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dailyfocus.core.ui.theme.DailyFocusTheme
import com.dailyfocus.presentation.navigation.MainScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity entry point for DailyFocus.
 * All navigation is handled within Compose via Navigation Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyFocusTheme {
                MainScreen()
            }
        }
    }
}
