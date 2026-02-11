package com.dailyfocus.presentation.today

import com.dailyfocus.core.util.UiMessage
import com.dailyfocus.domain.model.DailyTaskInstance
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import java.time.LocalDate

/**
 * Immutable UI state for the Today screen.
 * Single source of truth consumed by [TodayScreen].
 */
data class TodayUiState(
    val date: LocalDate = LocalDate.now(),
    val userName: String = "",
    val dailyChecklist: List<DailyTaskInstance> = emptyList(),
    val todayTasks: List<TodayTaskWithItems> = emptyList(),
    val categoryFilter: TaskCategory? = null,
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null
)

/**
 * A [TodayTask] bundled with its [TaskItem] children for display.
 */
data class TodayTaskWithItems(
    val task: TodayTask,
    val items: List<TaskItem> = emptyList()
)
