package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A goal containing a checklist of [GoalItem] milestones.
 */
data class Goal(
    val id: Long = 0,
    val title: String,
    val type: GoalType = GoalType.INFINITE,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
