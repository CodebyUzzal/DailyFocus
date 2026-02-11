package com.dailyfocus.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * A one-time task that exists only for a specific day.
 * When [recurringTaskId] is non-null, this task was auto-generated
 * from a [RecurringTask].
 *
 * @property id unique identifier
 * @property title display name
 * @property category Personal or Office classification
 * @property date the date this task belongs to
 * @property isCompleted whether the task is done
 * @property recurringTaskId FK to source RecurringTask (null for manual tasks)
 * @property createdAt audit timestamp
 */
data class TodayTask(
    val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val recurringTaskId: Long? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
