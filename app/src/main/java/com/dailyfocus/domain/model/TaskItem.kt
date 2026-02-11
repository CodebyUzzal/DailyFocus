package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A child item nested under a [TodayTask]. Only one level of nesting is allowed.
 * When all [TaskItem]s under a parent are completed, the parent auto-completes.
 *
 * @property id unique identifier
 * @property parentTaskId FK to the parent [TodayTask]
 * @property title display name
 * @property isCompleted whether this item is done
 * @property position stable ordering within the parent
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class TaskItem(
    val id: Long = 0,
    val parentTaskId: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val position: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
