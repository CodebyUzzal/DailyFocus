package com.dailyfocus.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.usecase.habits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val getHabitsWithStreaks: GetHabitsWithStreaksUseCase,
    private val toggleHabitForDate: ToggleHabitForDateUseCase,
    private val addEditHabit: AddEditHabitUseCase,
    private val deleteHabit: DeleteHabitUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

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

    fun onAddHabit(title: String, category: TaskCategory? = null) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addEditHabit(title = title.trim(), category = category)
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
                deleteHabit(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onMessageDismissed() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
