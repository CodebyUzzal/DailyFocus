package com.dailyfocus.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.usecase.today.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Today tab.
 * Uses only use cases — no repository access (clean architecture).
 */
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val dailyBoundaryManager: DailyBoundaryManager,
    private val getTodayTasks: GetTodayTasksUseCase,
    private val addTodayTask: AddTodayTaskUseCase,
    private val toggleTodayTask: ToggleTodayTaskUseCase,
    private val preferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val today = DateUtils.today()

    init {
        initializeDay()
        observeData()
    }

    /** Run boundary check on init to generate recurring task instances. */
    private fun initializeDay() {
        viewModelScope.launch {
            try {
                dailyBoundaryManager.onAppResume()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to initialize: ${e.message}"))
                }
            }
        }
    }

    /**
     * Observes userName, categoryFilter, and todayTasks.
     * Combines them cleanly to avoid race conditions and multiple subscriptions.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeData() {
        // 1. Observe User Name
        viewModelScope.launch {
            preferences.userName.collect { name ->
                _uiState.update { it.copy(userName = name ?: "") }
            }
        }

        // 2. Observe Filter & Tasks (chained)
        viewModelScope.launch {
            preferences.categoryFilter
                .map { filterString ->
                    val category = filterString?.let {
                        try { TaskCategory.valueOf(it) } catch (_: Exception) { null }
                    }
                    _uiState.update { it.copy(categoryFilter = category) }
                    category
                }
                .flatMapLatest { filter ->
                    getTodayTasks(today).map { allTasks ->
                        if (filter != null) {
                            allTasks.filter { it.category == filter }
                        } else {
                            allTasks
                        }
                    }
                }
                .catch { e ->
                     _uiState.update {
                        it.copy(userMessage = UiMessage.Snackbar("Error loading tasks: ${e.message}"))
                    }
                }
                .collect { filteredTasks ->
                    _uiState.update { it.copy(todayTasks = filteredTasks, isLoading = false) }
                }
        }
    }

    // ── User Actions ────────────────────────────────────────────────────

    fun onToggleTodayTask(task: TodayTask) {
        viewModelScope.launch {
            try {
                toggleTodayTask(task)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to update: ${e.message}"))
                }
            }
        }
    }

    fun onAddTask(title: String, category: TaskCategory) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addTodayTask(title.trim(), category, today)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to add task: ${e.message}"))
                }
            }
        }
    }

    fun onCategoryFilterChanged(category: TaskCategory?) {
        viewModelScope.launch {
            preferences.setCategoryFilter(category?.name)
        }
    }

    fun onMessageDismissed() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
