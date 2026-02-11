package com.dailyfocus.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.usecase.today.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Today tab.
 * Orchestrates daily checklist (recurring instances), one-time tasks,
 * category filtering, and day-boundary management.
 */
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val dailyBoundaryManager: DailyBoundaryManager,
    private val getRecurringInstances: GetTodayRecurringInstancesUseCase,
    private val toggleRecurringInstance: ToggleRecurringInstanceUseCase,
    private val updateRoutineStreak: UpdateRoutineStreakUseCase,
    private val getTodayTasks: GetTodayTasksUseCase,
    private val addTodayTask: AddTodayTaskUseCase,
    private val toggleTodayTask: ToggleTodayTaskUseCase,
    private val toggleTaskItem: ToggleTaskItemUseCase,
    private val getTaskItemsByParent: GetTaskItemsByParentUseCase,
    private val addTaskItem: AddTaskItemUseCase,
    private val preferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val today = DateUtils.today()

    init {
        initializeDay()
        observeCategoryFilter()
        observeData()
    }

    /** Run boundary check on init to generate missing instances. */
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
                // Re-observe data with new filter
                observeData()
            }
        }
    }

    /** Observe recurring instances and today tasks based on current filter. */
    private fun observeData() {
        val filter = _uiState.value.categoryFilter

        viewModelScope.launch {
            getRecurringInstances(today, filter).collect { instances ->
                _uiState.update { it.copy(dailyChecklist = instances, isLoading = false) }
            }
        }

        viewModelScope.launch {
            getTodayTasks(today, filter).collect { tasks ->
                val tasksWithItems = tasks.map { task ->
                    val items = getTaskItemsByParent(task.id)
                    TodayTaskWithItems(task = task, items = items)
                }
                _uiState.update { it.copy(todayTasks = tasksWithItems, isLoading = false) }
            }
        }
    }

    // ── User Actions ────────────────────────────────────────────────────

    fun onToggleRecurringInstance(instanceId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                toggleRecurringInstance(instanceId, !isCompleted)
                
                // Update streak if completing
                val instance = _uiState.value.dailyChecklist.find { it.id == instanceId }
                if (instance != null && !isCompleted) { // !isCompleted means we are setting it to true
                    // We need the routineId. DailyTaskInstance has recurringTaskId.
                    updateRoutineStreak(instance.recurringTaskId, today, true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to update: ${e.message}"))
                }
            }
        }
    }

    fun onToggleTodayTask(task: TodayTask) {
        viewModelScope.launch {
            try {
                toggleTodayTask.toggle(task)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to update: ${e.message}"))
                }
            }
        }
    }

    fun onToggleTaskItem(item: TaskItem, parentTask: TodayTask) {
        viewModelScope.launch {
            try {
                toggleTaskItem(item, parentTask)
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
                addTodayTask(
                    TodayTask(
                        title = title.trim(),
                        category = category,
                        date = today
                    )
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to add task: ${e.message}"))
                }
            }
        }
    }

    fun onAddTaskItem(parentTaskId: Long, title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addTaskItem(
                    TaskItem(parentTaskId = parentTaskId, title = title.trim())
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = UiMessage.Snackbar("Failed to add sub-item: ${e.message}"))
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
