package com.dailyfocus.presentation.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.DailyLogEntry
import com.dailyfocus.domain.usecase.log.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogUiState(
    val entries: List<DailyLogEntry> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

@HiltViewModel
class LogViewModel @Inject constructor(
    private val getDailyLogs: GetDailyLogsUseCase,
    private val addEditDailyLog: AddEditDailyLogUseCase,
    private val deleteDailyLog: DeleteDailyLogUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDailyLogs().collect { entries ->
                _uiState.update { it.copy(entries = entries, isLoading = false) }
            }
        }
    }

    fun onAddEntry(activityName: String, durationMinutes: Int, note: String?) {
        if (activityName.isBlank() || durationMinutes <= 0) return
        viewModelScope.launch {
            try {
                addEditDailyLog(
                    DailyLogEntry(
                        activityName = activityName.trim(),
                        durationMinutes = durationMinutes,
                        note = note?.trim()?.ifBlank { null },
                        date = DateUtils.today()
                    )
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed: ${e.message}"))
                }
            }
        }
    }

    fun onDeleteEntry(id: Long) {
        viewModelScope.launch {
            try {
                deleteDailyLog(id)
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
