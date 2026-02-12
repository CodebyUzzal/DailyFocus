package com.dailyfocus.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * A time/activity log entry for reflection and productivity tracking.
 *
 * @property id unique identifier
 * @property activityName what the user worked on
 * @property durationMinutes how long the activity lasted
 * @property note optional free-text note
 * @property date the date of the activity
 * @property createdAt audit timestamp
 */
data class DailyLogEntry(
    val id: Long = 0,
    val activityName: String,
    val durationMinutes: Int,
    val type: LogType = LogType.FOCUS,
    val note: String? = null,
    val date: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
