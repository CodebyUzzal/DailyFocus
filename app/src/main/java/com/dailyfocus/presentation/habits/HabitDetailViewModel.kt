package com.dailyfocus.presentation.habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isLoading: Boolean = true
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
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
