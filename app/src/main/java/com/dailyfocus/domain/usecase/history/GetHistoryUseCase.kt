package com.dailyfocus.domain.usecase.history

import com.dailyfocus.domain.model.HistoryItem
import com.dailyfocus.domain.repository.DailyLogRepository
import com.dailyfocus.domain.repository.HabitRepository
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val taskRepository: TodayTaskRepository,
    private val habitRepository: HabitRepository,
    private val logRepository: DailyLogRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<HistoryItem>> {
        return combine(
            taskRepository.getCompletedByDateRange(startDate, endDate),
            habitRepository.getLogsByDateRange(startDate, endDate),
            logRepository.getByDateRange(startDate, endDate),
            habitRepository.getAllHabits()
        ) { tasks, habitLogs, focusLogs, habits ->
            val habitMap = habits.associateBy { it.id }
            val items = mutableListOf<HistoryItem>()

            // 1. Process Tasks
            items.addAll(tasks.map { HistoryItem.ProcessedTask(it) })

            // 2. Process Habits
            items.addAll(habitLogs.mapNotNull { (habitId, date) ->
                habitMap[habitId]?.let { habit ->
                    HistoryItem.ProcessedHabit(habitId, habit.title, date)
                }
            })

            // 3. Process Focus Logs
            items.addAll(focusLogs.map { HistoryItem.ProcessedFocusSession(it) })

            // Sort by Date DESC
            // For items on the same date, we don't have exact time for Habits/Tasks,
            // so order is arbitrary between types, but consistent within types by ID.
            items.sortedWith(
                compareByDescending<HistoryItem> { it.date }
                    .thenByDescending { it.sortKey }
            )
        }
    }
}
