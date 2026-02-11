package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A single milestone within a [Goal].
 */
data class GoalItem(
    val id: Long = 0,
    val goalId: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
