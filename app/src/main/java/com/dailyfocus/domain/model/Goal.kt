package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A goal that acts as a container for checklist items.
 * Progress is derived from the completion ratio of its [GoalItem] children.
 *
 * @property id unique identifier
 * @property title display name (e.g. "Learn Kotlin Multiplatform")
 * @property type monthly, yearly, or infinite scope
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class Goal(
    val id: Long = 0,
    val title: String,
    val type: GoalType = GoalType.INFINITE,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
