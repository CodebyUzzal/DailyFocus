package com.dailyfocus.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.model.GoalType
import com.dailyfocus.domain.usecase.goals.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalsUiState(
    val goals: List<GoalWithProgress> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoals: GetGoalsUseCase,
    private val addEditGoal: AddEditGoalUseCase,
    private val toggleGoalItem: ToggleGoalItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getGoals().collect { goals ->
                _uiState.update { it.copy(goals = goals, isLoading = false) }
            }
        }
    }

    fun onAddGoal(title: String, type: GoalType, deadline: java.time.LocalDate? = null) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addEditGoal.addGoal(title.trim(), type, deadline)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onDeleteGoal(id: Long) {
        viewModelScope.launch {
            try {
                addEditGoal.deleteGoal(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onAddGoalItem(goalId: Long, title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addEditGoal.addItem(goalId, title.trim())
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onToggleGoalItem(item: GoalItem) {
        viewModelScope.launch {
            try {
                toggleGoalItem(item)
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
