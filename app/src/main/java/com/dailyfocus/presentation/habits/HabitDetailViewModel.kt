package com.dailyfocus.presentation.habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.repository.HabitRepository
import com.dailyfocus.domain.usecase.habits.HabitWithStreak
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HabitDetailUiState(
    val habit: HabitWithStreak? = null,
    val completedDates: Set<LocalDate> = emptySet(),
    val completionRate: Int = 0,
    val currentWeekCount: Int = 0,
    val totalLogs: Int = 0,
    val selectedMonth: java.time.YearMonth = java.time.YearMonth.now(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val habitId: Long = savedStateHandle.get<Long>("habitId") ?: -1L

    private val _uiState = MutableStateFlow(HabitDetailUiState())
    val uiState: StateFlow<HabitDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        if (habitId > 0) {
            viewModelScope.launch {
                val habit = habitRepository.getHabitById(habitId)
                if (habit != null) {
                    val today = LocalDate.now()
                    val completedToday = habitRepository.hasLogForDate(habitId, today)
                    val allLogDates = habitRepository.getLogDatesForHabit(habitId).toSet()

                    // Calculate last 7 days history
                    val start = today.minusDays(6)
                    val history = (0..6).map { i ->
                        allLogDates.contains(start.plusDays(i.toLong()))
                    }

                    // Calculate stats
                    val totalLogs = allLogDates.size
                    
                    // Calculate completion rate (last 30 days, restricted by creation date)
                    val createdAtDate = habit.createdAt.toLocalDate()
                    val last30Days = (0..29).map { today.minusDays(it.toLong()) }
                    
                    // Only consider days that are on or after the creation date
                    val validDaysToCheck = last30Days.filter { !it.isBefore(createdAtDate) }
                    val completedInValidPeriod = validDaysToCheck.count { allLogDates.contains(it) }
                    
                    // Avoid division by zero if habit created today
                    val denominator = validDaysToCheck.size
                    val rate = if (denominator > 0) (completedInValidPeriod * 100 / denominator) else 0

                    // Calculate current week (Monday to Sunday)
                    val currentDayOfWeek = today.dayOfWeek.value // 1 (Mon) - 7 (Sun)
                    val startOfWeek = today.minusDays((currentDayOfWeek - 1).toLong())
                    val currentWeekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }
                    val thisWeekCount = currentWeekDates.count { allLogDates.contains(it) }

                    _uiState.update {
                        it.copy(
                            habit = HabitWithStreak(
                                habit = habit,
                                currentStreak = habit.currentStreak,
                                longestStreak = habit.longestStreak,
                                completedToday = completedToday,
                                last7Days = history
                            ),
                            completedDates = allLogDates,
                            completionRate = rate,
                            currentWeekCount = thisWeekCount,
                            totalLogs = totalLogs,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun toggleHabitLog(date: LocalDate) {
        viewModelScope.launch {
            try {
                if (_uiState.value.completedDates.contains(date)) {
                    habitRepository.deleteLog(habitId, date)
                } else {
                    habitRepository.insertLog(habitId, date)
                }
                // Reload data to refresh stats and streak
                loadData()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to update: ${e.message}"))
                }
            }
        }
    }

    fun onMonthChanged(newMonth: java.time.YearMonth) {
        _uiState.update { it.copy(selectedMonth = newMonth) }
    }

    fun onMessageDismissed() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
