package com.dailyfocus.domain.model

import java.time.LocalDate

/**
 * A concrete instance of a [RecurringTask] for a specific date.
 * This is the daily completion record — separate from the template.
 * History is preserved: editing or deleting the template does NOT erase past instances.
 *
 * @property id unique identifier
 * @property recurringTaskId FK to the parent [RecurringTask]
 * @property date the date this instance belongs to
 * @property isCompleted whether the user marked this done
 * @property title snapshot of the task title at generation time
 * @property category snapshot of the task category at generation time
 */
data class DailyTaskInstance(
    val id: Long = 0,
    val recurringTaskId: Long,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val title: String,
    val category: TaskCategory
)
