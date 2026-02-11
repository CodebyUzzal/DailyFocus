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
        observeCategoryFilter()
        observeUserName()
        observeData()
    }

    /** Observe the user's display name from DataStore. */
    private fun observeUserName() {
        viewModelScope.launch {
            preferences.userName.collect { name ->
                _uiState.update { it.copy(userName = name ?: "") }
            }
        }
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

    /** Observe persisted category filter from DataStore. */
    private fun observeCategoryFilter() {
        viewModelScope.launch {
            preferences.categoryFilter.collect { filter ->
                val category = filter?.let {
                    try { TaskCategory.valueOf(it) } catch (_: Exception) { null }
                }
                _uiState.update { it.copy(categoryFilter = category) }
                observeData()
            }
        }
    }

    /** Observe today tasks based on current filter. */
    private fun observeData() {
        viewModelScope.launch {
            getTodayTasks(today).collect { allTasks ->
                val filter = _uiState.value.categoryFilter
                val filtered = if (filter != null) {
                    allTasks.filter { it.category == filter }
                } else {
                    allTasks
                }
                _uiState.update { it.copy(todayTasks = filtered, isLoading = false) }
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
