package com.dailyfocus.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.Description
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Typed route definitions for Navigation Compose.
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Today : Screen("today", "Today", Icons.Default.CheckCircle)
    data object Habits : Screen("habits", "Habits", Icons.Default.CalendarMonth)
    data object Goals : Screen("goals", "Goals", Icons.Default.Flag)
    data object Log : Screen("log", "Log", Icons.Default.Schedule)
    data object Notes : Screen("notes", "Notes", androidx.compose.material.icons.Icons.Rounded.Description)

    companion object {
        val bottomNavItems get() = listOf(Today, Habits, Goals, Log, Notes)
    }
}

/** Non-bottom-nav destinations */
object Destinations {
    const val ONBOARDING = "onboarding"
    const val ROUTINES = "routines"
    const val HABIT_DETAIL = "habit_detail/{habitId}"
    const val HISTORY = "history"
    const val ABOUT = "about"
    const val NOTE_EDITOR = "note_editor/{noteId}?isChecklist={isChecklist}"
    
    fun habitDetail(habitId: Long) = "habit_detail/$habitId"
    fun noteEditor(noteId: Long, isChecklist: Boolean = false) = "note_editor/$noteId?isChecklist=$isChecklist"
}
