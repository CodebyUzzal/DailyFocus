package com.dailyfocus.presentation.today.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.Routine
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.usecase.today.ManageRoutinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoutineUiState(
    val routines: List<Routine> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val manageRoutines: ManageRoutinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineUiState())
    val uiState: StateFlow<RoutineUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            manageRoutines.getAll().collect { routines ->
                _uiState.update { it.copy(routines = routines, isLoading = false) }
            }
        }
    }

    fun onAddRoutine(title: String, category: TaskCategory) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                manageRoutines.addRoutine(
                    Routine(
                        title = title.trim(),
                        category = category,
                        position = _uiState.value.routines.size
                    )
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to add: ${e.message}"))
                }
            }
        }
    }

    fun onUpdateRoutine(routine: Routine) {
        viewModelScope.launch {
            try {
                manageRoutines.updateRoutine(routine)
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
                manageRoutines.deleteRoutine(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to delete: ${e.message}"))
                }
            }
        }
    }

    fun onToggleActive(routine: Routine) {
        onUpdateRoutine(routine.copy(isActive = !routine.isActive))
    }

    fun onMessageDismissed() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
