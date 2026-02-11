package com.dailyfocus.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.model.HabitFrequency
import com.dailyfocus.domain.repository.HabitRepository
import com.dailyfocus.domain.usecase.habits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val getHabitsWithStreaks: GetHabitsWithStreaksUseCase,
    private val toggleHabitForDate: ToggleHabitForDateUseCase,
    private val addEditHabit: AddEditHabitUseCase,
    private val getHabitCalendar: GetHabitCalendarUseCase,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    private val _detailState = MutableStateFlow(HabitDetailUiState())
    val detailState: StateFlow<HabitDetailUiState> = _detailState.asStateFlow()

    init {
        viewModelScope.launch {
            getHabitsWithStreaks().collect { habits ->
                _uiState.update { it.copy(habits = habits, isLoading = false) }
            }
        }
    }

    fun onToggleToday(habitId: Long) {
        viewModelScope.launch {
            try {
                toggleHabitForDate(habitId, DateUtils.today())
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onAddHabit(name: String, frequency: HabitFrequency) {
        if (name.isBlank()) return
        viewModelScope.launch {
            try {
                addEditHabit(Habit(name = name.trim(), frequency = frequency))
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onDeleteHabit(id: Long) {
        viewModelScope.launch {
            try {
                habitRepository.deleteHabit(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun loadHabitDetail(habitId: Long) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true) }
            val today = DateUtils.today()
            val startDate = today.minusDays(89) // 90-day window
            val calendar = getHabitCalendar(habitId, startDate, today)
            val habit = _uiState.value.habits.find { it.habit.id == habitId }
            _detailState.update {
                it.copy(habit = habit, calendarDays = calendar, isLoading = false)
            }
        }
    }

    fun onMessageDismissed() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
