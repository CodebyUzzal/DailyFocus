package com.dailyfocus.presentation.today.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.RecurringTask
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.usecase.today.GenerateRecurringTasksUseCase
import com.dailyfocus.domain.usecase.today.ManageRecurringTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

data class RoutineUiState(
    val routines: List<RecurringTask> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val manageRecurringTasks: ManageRecurringTasksUseCase,
    private val generateRecurringTasks: GenerateRecurringTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineUiState())
    val uiState: StateFlow<RoutineUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            manageRecurringTasks.getAll().collect { tasks ->
                _uiState.update { it.copy(routines = tasks, isLoading = false) }
            }
        }
    }

    fun onAddRoutine(title: String, category: TaskCategory, daysOfWeek: Set<DayOfWeek> = emptySet()) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                manageRecurringTasks.add(title.trim(), category, daysOfWeek)
                // Also generate today's task if applicable
                generateRecurringTasks()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to add: ${e.message}"))
                }
            }
        }
    }

    fun onUpdateRoutine(task: RecurringTask) {
        viewModelScope.launch {
            try {
                manageRecurringTasks.update(task)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to update: ${e.message}"))
                }
            }
        }
    }

    fun onDeleteRoutine(id: Long) {
        viewModelScope.launch {
            try {
                manageRecurringTasks.delete(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to delete: ${e.message}"))
                }
            }
        }
    }

    fun onToggleActive(task: RecurringTask) {
        viewModelScope.launch {
            try {
                manageRecurringTasks.toggleActive(task)
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
