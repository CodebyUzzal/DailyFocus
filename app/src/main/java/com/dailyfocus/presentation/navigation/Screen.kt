package com.dailyfocus.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Typed route definitions for Navigation Compose.
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Today : Screen("today", "Today", Icons.Default.CheckCircle)
    data object Habits : Screen("habits", "Habits", Icons.Default.CalendarMonth)
    data object Goals : Screen("goals", "Goals", Icons.Default.Flag)
    data object Log : Screen("log", "Log", Icons.Default.Schedule)

    companion object {
        val bottomNavItems = listOf(Today, Habits, Goals, Log)
    }
}

/** Non-bottom-nav destinations */
object Destinations {
    const val ONBOARDING = "onboarding"
    const val ROUTINES = "routines"
    const val HABIT_DETAIL = "habit_detail/{habitId}"
    const val HISTORY = "history"
    fun habitDetail(habitId: Long) = "habit_detail/$habitId"
}
