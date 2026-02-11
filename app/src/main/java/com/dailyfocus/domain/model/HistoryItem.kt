package com.dailyfocus.domain.model

import java.time.LocalDate

/**
 * Represents a historical event in the user's timeline.
 * Aggregates completed tasks, habit completions, and focus sessions.
 */
sealed class HistoryItem {
    abstract val date: LocalDate
    abstract val sortKey: Long // For stable sorting/id

    data class ProcessedTask(
        val task: TodayTask
    ) : HistoryItem() {
        override val date: LocalDate = task.date
        override val sortKey: Long = task.id // Use ID as secondary sort
    }

    data class ProcessedHabit(
        val habitId: Long,
        val title: String,
        override val date: LocalDate
    ) : HistoryItem() {
        override val sortKey: Long = habitId
    }

    data class ProcessedFocusSession(
        val entry: DailyLogEntry
    ) : HistoryItem() {
        override val date: LocalDate = entry.date
        override val sortKey: Long = entry.id
    }
}
