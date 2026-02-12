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
    val filter: com.dailyfocus.domain.model.LogType? = null,
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

    private val _filter = MutableStateFlow<com.dailyfocus.domain.model.LogType?>(null)

    init {
        viewModelScope.launch {
            combine(getDailyLogs(), _filter) { entries, filterType ->
                // Sort by date descending, then created at descending
                val sorted = entries.sortedWith(compareByDescending<DailyLogEntry> { it.date }.thenByDescending { it.createdAt })
                if (filterType == null) sorted else sorted.filter { it.type == filterType }
            }.collect { filteredEntries ->
                _uiState.update { it.copy(entries = filteredEntries, isLoading = false) }
            }
        }
    }

    fun onFilterChanged(type: com.dailyfocus.domain.model.LogType?) {
        _filter.value = type
        _uiState.update { it.copy(filter = type) }
    }

    fun onAddEntry(activityName: String, durationMinutes: Int, type: com.dailyfocus.domain.model.LogType, note: String?) {
        if (activityName.isBlank() || durationMinutes <= 0) return
        viewModelScope.launch {
            try {
                addEditDailyLog(
                    DailyLogEntry(
                        activityName = activityName.trim(),
                        durationMinutes = durationMinutes,
                        type = type,
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
