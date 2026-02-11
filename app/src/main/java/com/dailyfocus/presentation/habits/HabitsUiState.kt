package com.dailyfocus.presentation.habits

import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.usecase.habits.HabitCalendarDay
import com.dailyfocus.domain.usecase.habits.HabitWithStreak

data class HabitsUiState(
    val habits: List<HabitWithStreak> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

data class HabitDetailUiState(
    val habit: HabitWithStreak? = null,
    val calendarDays: List<HabitCalendarDay> = emptyList(),
    val isLoading: Boolean = true
)
