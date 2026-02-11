package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A checklist item inside a [Goal].
 * Toggling items updates the parent goal's progress percentage.
 *
 * @property id unique identifier
 * @property goalId FK to the parent [Goal]
 * @property title display text
 * @property isCompleted whether this item is done
 * @property position stable ordering within the goal
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class GoalItem(
    val id: Long = 0,
    val goalId: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val position: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
