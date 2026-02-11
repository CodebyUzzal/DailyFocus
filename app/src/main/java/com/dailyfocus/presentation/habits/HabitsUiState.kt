package com.dailyfocus.presentation.habits

import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.usecase.habits.HabitWithStreak

data class HabitsUiState(
    val habits: List<HabitWithStreak> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)
