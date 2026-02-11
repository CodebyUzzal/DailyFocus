package com.dailyfocus.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.domain.model.HistoryItem
import com.dailyfocus.domain.usecase.history.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(90) // Load last 3 months

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getHistoryUseCase(startDate, endDate).collect { items ->
                val grouped = items.groupBy { it.date }.toSortedMap(compareByDescending { it })
                _uiState.update { 
                    it.copy(historyItems = grouped, isLoading = false) 
                }
            }
        }
    }
}

data class HistoryUiState(
    val historyItems: Map<LocalDate, List<HistoryItem>> = emptyMap(),
    val isLoading: Boolean = true
)
